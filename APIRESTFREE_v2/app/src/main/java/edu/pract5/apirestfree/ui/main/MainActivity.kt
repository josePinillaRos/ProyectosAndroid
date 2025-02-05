package edu.pract5.apirestfree.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pract5.apirestfree.R
import edu.pract5.apirestfree.RoomApplication
import edu.pract5.apirestfree.data.LocalDataSource
import edu.pract5.apirestfree.data.RemoteDataSource
import edu.pract5.apirestfree.data.Repository
import edu.pract5.apirestfree.databinding.ActivityMainBinding
import edu.pract5.apirestfree.model.Animals
import edu.pract5.apirestfree.utils.ListaAMostrar
import edu.pract5.apirestfree.utils.OrdenAlfabetico
import edu.pract5.apirestfree.utils.checkConnection
import edu.pract5.apirestfree.utils.listaAMostrar
import edu.pract5.apirestfree.utils.ordenAlfabetico
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        val db = (application as RoomApplication).animalsDatabase
        val remoteDataSource = RemoteDataSource()
        val localDataSource = LocalDataSource(db.animalsDao())
        val repository = Repository(remoteDataSource, localDataSource)
        MainViewModelFactory(repository)
    }

    private lateinit var adapter: AnimalsAdapter

    private var searchInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = AnimalsAdapter(
            onClickAnimalsItem = { animal ->
                showAnimalDialog(animal)
            },
            onClickFavorite = { animal ->
                val newFav = !animal.favorita
                animal.favorita = newFav
                vm.updateFavoriteAnimal(
                    animal.copy(favorita = newFav)
                )
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Primera carga de animales
        lifecycleScope.launch {
            combine(vm.animals, vm.favoriteAnimals) { animalsApi, favoritesDb ->
                animalsApi.forEach { animal ->
                    val isFavorite = favoritesDb.any { it.name == animal.name }
                    animal.favorita = isFavorite
                }

                // Determinamos qué lista mostrar
                if (listaAMostrar == ListaAMostrar.TODAS) {
                    animalsApi
                } else {
                    favoritesDb
                }
            }
            .catch { e ->
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            .collect { combinedList ->
                // Si venimos de una búsqueda y la lista está vacía, mostramos el Toast
                if (combinedList.isEmpty() && searchInProgress) {
                    Toast.makeText(
                        this@MainActivity,
                        "SIN RESULTADOS, mostrando lista inicial",
                        Toast.LENGTH_LONG
                    ).show()
                    vm.getAnimals()
                    searchInProgress = false
                }

                // Ordenamos la lista alfabeticamente
                val sortedAnimals = when (ordenAlfabetico) {
                    OrdenAlfabetico.ASCENDENTE ->
                        combinedList.sortedBy { it.name?.uppercase() ?: "" }
                    OrdenAlfabetico.DESCENDENTE ->
                        combinedList.sortedByDescending { it.name?.uppercase() ?: "" }
                }
                // Actualizamos el adaptador
                adapter.submitList(sortedAnimals) {
                    binding.recyclerView.scrollToPosition(0)
                }
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.swipeRefresh.setOnRefreshListener {
            getAnimals()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val processedQuery = query?.trim()?.lowercase() ?: ""
                    if (processedQuery.isNotEmpty()) {
                        searchInProgress = true
                        vm.updateListAnimals(processedQuery)
                    } else {
                        vm.getAnimals()
                    }
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )

        binding.mBottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.op_todas -> {
                    listaAMostrar = ListaAMostrar.TODAS
                    binding.searchView.visibility = View.VISIBLE
                    binding.searchView.isEnabled = true
                    binding.searchView.clearFocus()
                    getAnimals()
                    true
                }
                R.id.op_favoritas -> {
                    listaAMostrar = ListaAMostrar.FAVORITAS
                    binding.searchView.visibility = View.GONE
                    getAnimals()
                    true
                }
                else -> false
            }
        }

        binding.mToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.opt_ordenar_alfabeticamente -> {
                    ordenAlfabetico = if (ordenAlfabetico == OrdenAlfabetico.ASCENDENTE) {
                        OrdenAlfabetico.DESCENDENTE
                    } else {
                        OrdenAlfabetico.ASCENDENTE
                    }
                    getAnimals()
                    true
                }
                R.id.opt_acerca_de -> {
                    AlertDialog.Builder(this)
                        .setTitle("Acerca de")
                        .setMessage("Práctica 5 - API REST Free\n\n" +
                                "Desarrollado por:\n" +
                                "  - Jose Pinilla\n"
                                + " 2º DAM\n\n "
                                + " - Curso: 2024-2025\n\n"
                                + " - Asignatura: Programación Multimedia\n\n")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                    true
                }
                else -> false
            }
        }
    }

    private fun getAnimals() {
        lifecycleScope.launch {
            combine(vm.animals, vm.favoriteAnimals) { animalsApi, favoritesDb ->
                animalsApi.forEach { animal ->
                    val isFavorite = favoritesDb.any { it.name == animal.name }
                    animal.favorita = isFavorite
                }
                if (listaAMostrar == ListaAMostrar.TODAS) {
                    animalsApi
                } else {
                    favoritesDb
                }
            }
                .catch { e ->
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                .collect { combinedList ->
                    val sortedAnimals = when (ordenAlfabetico) {
                        OrdenAlfabetico.ASCENDENTE ->
                            combinedList.sortedBy { it.name?.uppercase() ?: "" }
                        OrdenAlfabetico.DESCENDENTE ->
                            combinedList.sortedByDescending { it.name?.uppercase() ?: "" }
                    }
                    adapter.submitList(sortedAnimals) {
                        binding.recyclerView.scrollToPosition(0)
                    }
                }
        }
    }

    private fun showAnimalDialog(animal: Animals) {
        val dialogView = layoutInflater.inflate(R.layout.animal_dialog, null)

        // Configuramos los textos en el diseño
        val tvAnimalName = dialogView.findViewById<TextView>(R.id.tv_animal_name)
        val tvTaxonomyInfo = dialogView.findViewById<TextView>(R.id.tv_taxonomy_info)
        val tvCharacteristicsInfo = dialogView.findViewById<TextView>(R.id.tv_characteristics_info)
        val tvLocationInfo = dialogView.findViewById<TextView>(R.id.tv_location_info)

        // Asignamos valores
        tvAnimalName.text = animal.name ?: "N/A"
        tvTaxonomyInfo.text = animal.taxonomy?.let {
            """
        - Kingdom: ${it.kingdom ?: "N/A"}
        - Phylum: ${it.phylum ?: "N/A"}
        - Class: ${it.classX ?: "N/A"}
        - Order: ${it.order ?: "N/A"}
        - Family: ${it.family ?: "N/A"}
        - Genus: ${it.genus ?: "N/A"}
        - Scientific Name: ${it.scientificName ?: "N/A"}
        """.trimIndent()
        } ?: " N/A"

        tvCharacteristicsInfo.text = animal.characteristics?.let {
            """
        - Common Name: ${it.commonName ?: "N/A"}
        - Lifespan: ${it.lifespan ?: "N/A"}
        - Habitat: ${it.habitat ?: "N/A"}
        - Diet: ${it.diet ?: "N/A"}
        - Estimated Population: ${it.estimatedPopulationSize ?: "N/A"}
        - Biggest Threat: ${it.biggestThreat ?: "N/A"}
        """.trimIndent()
        } ?: " N/A"

        tvLocationInfo.text = animal.locations?.joinToString(
            prefix = " - ",
            separator = "\n  - "
        ) ?: " N/A"

        // Construimos el diálogo
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}

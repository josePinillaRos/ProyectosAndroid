package edu.pract5.apirestfree.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
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

    // ViewModel
    private val vm: MainViewModel by viewModels {
        val db = (application as RoomApplication).animalsDatabase
        val remoteDataSource = RemoteDataSource()
        val localDataSource = LocalDataSource(db.animalsDao())
        val repository = Repository(remoteDataSource, localDataSource)
        MainViewModelFactory(repository)
    }

    // Adaptador
    private lateinit var adapter: AnimalsAdapter




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
                Toast.makeText(this, "Favorite ${!animal.favorita}", Toast.LENGTH_SHORT).show()
                val newFav = !animal.favorita
                animal.favorita = newFav
                vm.updateFavoriteAnimal(Animals(
                    name = animal.name,
                    favorita = newFav,
                    characteristics = animal.characteristics,
                    taxonomy = animal.taxonomy,
                    locations = animal.locations
                ))
                Log.i("MainActivity", "onClickFavorite - Name: ${animal.favorita} => $newFav")
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            combine(vm.animals, vm.favoriteAnimals) { animalsFromApi, favoritesFromDb ->
                // Marcar favoritos en los animales de la API
                animalsFromApi.forEach { animal ->
                    val isFavorite = favoritesFromDb.any { it.name == animal.name }
                    animal.favorita = isFavorite
                }
                animalsFromApi
            }.collect { updatedAnimals ->
                adapter.submitList(updatedAnimals)
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
                        // Llamamos a updateListAnimals => actualiza vm.animals con la búsqueda
                        vm.updateListAnimals(processedQuery)

                        // Observamos los resultados en vm.animals para manejar el caso de búsqueda vacía
                        lifecycleScope.launch {
                            vm.animals.collect { animals ->
                                if (animals.isEmpty()) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "No se encontraron resultados, mostrando lista inicial",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Volvemos a mostrar el primer animal
                                    vm.getAnimals()
                                }
                            }
                        }
                    } else {
                        // Si la búsqueda está vacía, volvemos a getFirstAnimals() => primer animal
                        vm.getAnimals()
                    }
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // No hacemos búsqueda en vivo
                    return false
                }
            }
        )

        // BottomNavigation: TODAS o FAVORITAS
        binding.mBottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.op_todas -> {
                    listaAMostrar = ListaAMostrar.TODAS

                    // Mostrar la SearchView
                    binding.searchView.visibility = View.VISIBLE
                    binding.searchView.isEnabled = true
                    binding.searchView.clearFocus()

                    getAnimals() // mostráremos la lista en modo "todas" (API + favoritos)
                    true
                }
                R.id.op_favoritas -> {
                    listaAMostrar = ListaAMostrar.FAVORITAS

                    // Ocultar la SearchView
                    binding.searchView.visibility = View.GONE

                    getAnimals() // mostráremos sólo los de la BD (ignorando la búsqueda)
                    true
                }
                else -> false
            }
        }
    }

    /**
     * getAnimals()
     * Decide qué mostrar según 'listaAMostrar':
     *  - TODAS => combinamos vm.animals (API) con vm.favoriteAnimals (BD) para marcar favoritos
     *  - FAVORITAS => mostramos directamente todos los de BD, ignorando la búsqueda
     */
    private fun getAnimals() {
        Log.i("MainActivity", "getAnimals")
        adapter.submitList(emptyList())

        lifecycleScope.launch {
            if (checkConnection(this@MainActivity)) {
                // Mantén o quita este 'isEnabled' según tu lógica
                binding.swipeRefresh.isEnabled = (listaAMostrar == ListaAMostrar.TODAS)

                // Unimos en un único collect: combinamos (vm.animals, vm.favoriteAnimals)
                combine(vm.animals, vm.favoriteAnimals) { animalsApi, favoritesDb ->
                    // Construimos la lista final dependiendo de si queremos TODAS o FAVORITAS

                    if (listaAMostrar == ListaAMostrar.TODAS) {
                        // Para la lista TODAS lo que venga de la API
                        animalsApi.forEach { a ->
                            val match = favoritesDb.find { it.name == a.name }
                            a.favorita = match?.favorita ?: false
                        }
                        animalsApi // devolvemos la lista proveniente de la API
                    } else {
                        // Para la lista FAVORITAS directamente la lista de BD
                        favoritesDb
                    }
                }
                .catch { e ->
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    binding.swipeRefresh.isRefreshing = false
                }

            } else {
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.txt_noConnection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * showAnimalDialog
     * Muestra un diálogo con la info de un animal.
     */
    private fun showAnimalDialog(animal: Animals) {
        Log.i("MainActivity", "showAnimalDialog - Name: ${animal.name}")

        val taxonomyInfo = animal.taxonomy?.let {
            "Taxonomy:\n" +
                    "  - Kingdom: ${it.kingdom ?: "N/A"}\n" +
                    "  - Phylum: ${it.phylum ?: "N/A"}\n" +
                    "  - Class: ${it.classX ?: "N/A"}\n" +
                    "  - Order: ${it.order ?: "N/A"}\n" +
                    "  - Family: ${it.family ?: "N/A"}\n" +
                    "  - Genus: ${it.genus ?: "N/A"}\n" +
                    "  - Scientific Name: ${it.scientificName ?: "N/A"}"
        } ?: "Taxonomy: N/A"

        val characteristicsInfo = animal.characteristics?.let {
            "Characteristics:\n" +
                    "  - Common Name: ${it.commonName ?: "N/A"}\n" +
                    "  - Lifespan: ${it.lifespan ?: "N/A"}\n" +
                    "  - Habitat: ${it.habitat ?: "N/A"}\n" +
                    "  - Diet: ${it.diet ?: "N/A"}\n" +
                    "  - Estimated Population: ${it.estimatedPopulationSize ?: "N/A"}\n" +
                    "  - Biggest Threat: ${it.biggestThreat ?: "N/A"}"
        } ?: "Characteristics: N/A"

        val locationInfo = animal.locations?.joinToString(
            prefix = "Locations:\n  - ",
            separator = "\n  - "
        ) ?: "Locations: N/A"

        val message = """
            Name: ${animal.name ?: "N/A"}
            
            $taxonomyInfo

            $characteristicsInfo

            $locationInfo
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(animal.name ?: getString(R.string.txt_animal))
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}

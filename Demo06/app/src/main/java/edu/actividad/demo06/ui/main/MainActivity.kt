package edu.actividad.demo06.ui.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import edu.actividad.demo06.CityApplication
import edu.actividad.demo06.data.RemoteDataSource
import edu.actividad.demo06.data.Repository
import edu.actividad.demo06.databinding.ActivityMainBinding
import edu.actividad.demo06.ui.maps.DetailMapActivity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        CitiesAdapter(
            onCityClick = { city ->
                vm.addCity(city.name!!, city.country!!)
                DetailMapActivity.navigate(this@MainActivity, city)
            }
        )
    }
    private var query: String? = null

    private val vm: MainViewModel by viewModels {
        val db = (application as CityApplication).cityDB
        val ds = RemoteDataSource()
        MainViewModelFactory(Repository(db, ds))
    }

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

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        binding.mRecycler.setHasFixedSize(true)
        binding.mRecycler.adapter = adapter
        lifecycleScope.launch {
            populateCities()
        }

        binding.mRecycler.itemAnimator!!.apply {
            changeDuration = 0
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                populateCities()
            }
        }
    }

    // Método encargado de obtener los datos iniciales y búsqueda
    private suspend fun populateCities() {
        combine(
            vm.currentCities,
            vm.currentVisitedCities
        ) { cities, visitedCities ->
            Log.e(TAG, "Ciudades actualizadas: ${visitedCities.size} - $visitedCities")

            if (cities.isEmpty()) {
                binding.tvNoInfo.visibility = View.VISIBLE
            } else {
                binding.tvNoInfo.visibility = View.GONE
            }

            // A new list of cities with the visited field updated (si aplica)
            val updatedCities = cities.map { city ->
                val cityFound = visitedCities.find {
                    it["name"] == city.name && it["countryCode"] == city.country
                }
                if (cityFound != null) {
                    // Se usa copy para crear un nuevo objeto con visited incrementado
                    city.copy(visited = city.visited + 1)
                } else {
                    city
                }
            }

            Log.i(TAG, "populateCities: $updatedCities")

            // DEVUELVES la lista actualizada para que `combine` emita ese valor
            updatedCities
        }
            .catch { error ->
                // Maneja el error de la forma que consideres
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
            // AHORA recoges el valor emitido por `combine` en `collect`
            .collect { updatedCities ->
                // Aquí actualizas tu adapter
                adapter.submitList(updatedCities)
            }
    }



    override fun onStart() {
        super.onStart()
        // Gestión de la búsqueda.
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?):
                        Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?):
                        Boolean {
                    Log.d(TAG, "onQueryTextChange: $newText")
                    query = newText
                    vm.updateListCities(query!!)
                    lifecycleScope.launch {
                        populateCities()
                    }
                    return true
                }
            })
    }
}
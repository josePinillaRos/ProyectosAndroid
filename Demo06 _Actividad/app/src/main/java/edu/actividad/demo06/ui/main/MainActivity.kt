package edu.actividad.demo06.ui.main

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import edu.actividad.demo06.utils.createNotificationChannel
import edu.actividad.demo06.utils.sendNotification
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * MainActivity
 * Clase que representa la actividad principal
 *
 * @author Jose Pinilla
 */
class MainActivity : AppCompatActivity() {
    private val notificationRequestCode = 1001
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

    /**
     * onCreate
     * Método que se ejecuta al crear la actividad
     *
     * @author Jose Pinilla
     */
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
        //Crear canal de notificación
        createNotificationChannel(this)

        // Se comprueba si la versión es mayor o igual a TIRAMISU
        // para solicitar permiso de notificación en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    notificationRequestCode
                )
            }
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

    /**
     * populateCities
     * Método que actualiza la lista de ciudades
     *
     * @autor Jose Pinilla
     */
    private suspend fun populateCities() {
        // Combina las ciudades y todas las cities
        combine(
            vm.currentCities,
            vm.currentAllCitiesDocs
        ) { cities, allCitiesDocs ->
            val globalMap = allCitiesDocs.flatMap { mapEntry ->
                mapEntry.entries
            }.associate { (cityKey, count) ->
                Pair(
                    "${cityKey["name"]}_${cityKey["countryCode"]}",
                    count
                )
            }

            // Actualiza las ciudades con las visitas globales
            val updatedCities = cities.map { city ->
                val compositeKey = "${city.name}_${city.country}"
                val globalVisits = globalMap[compositeKey] ?: 0
                city.copy(visited = globalVisits)
            }

            if (updatedCities.isEmpty()) {
                binding.tvNoInfo.visibility = View.VISIBLE
            } else {
                binding.tvNoInfo.visibility = View.GONE
            }

            updatedCities
        }
        .catch { error ->
            Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
        }
        // Actualiza el adaptador con las ciudades y envía una notificación
        .collect { updatedCities ->
            adapter.submitList(updatedCities)
            sendNotification(this@MainActivity)
        }
    }

    /**
     * onStart
     * Método que se ejecuta al iniciar la actividad
     *
     * @autor Jose Pinilla
     */
    override fun onStart() {
        super.onStart()
        binding.searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?):
                            Boolean {
                        return false
                    }

                /**
                 * onQueryTextChange
                 * actualiza la lista de ciudades después de cada cambio en el texto de búsqueda
                 *
                 * @author Jose Pinilla
                 */
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
            }
        )
    }
}
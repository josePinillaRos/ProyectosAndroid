package edu.josepinilla.demo05.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import edu.josepinilla.demo05.R
import edu.josepinilla.demo05.RoomApplication
import edu.josepinilla.demo05.adapters.ShowsAdapter
import edu.josepinilla.demo05.data.RemoteDataSource
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.databinding.ActivityMainBinding
import edu.josepinilla.demo05.model.StateShows
import edu.josepinilla.demo05.ui.detail.DetailShowActivity
import edu.josepinilla.demo05.utils.checkConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * class MainActivity
 * se encarga de manejar la actividad principal de la aplicación
 *
 * @autor Jose Pinilla
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        val db = (application as RoomApplication).stateShowsDB.stateShowsDao()
        val dataSource = RemoteDataSource(db)
        val repository = Repository(dataSource)
        MainViewModelFactory(repository)
    }

    private val adapter = ShowsAdapter(
        onClickShowItem = {
            DetailShowActivity.navigate(this, it)
        },
        onClickShowFavorite = { show ->
            vm.updateStateShows(
                StateShows(
                    idShow = show.id!!,
                    stateFavorite = !show.favorite,
                    stateWatched = show.watched
                )
            )
        },
        onClickShowWatch = { show ->
            vm.updateStateShows(
                StateShows(
                    idShow = show.id!!,
                    stateFavorite = show.favorite,
                    stateWatched = !show.watched
                )
            )
        }
    )

    /**
     * fun onCreate
     * se encarga de crear la actividad principal
     *
     * @param savedInstanceState estado de la actividad
     *
     * @autor Jose Pinilla
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Infla el menú en la toolbar
        binding.mToolbar.inflateMenu(R.menu.menu)

        // Configura el listener para los ítems del menú
        binding.mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                // Si es ALPHABETICO_ASC, se invierte a ALPHABETICo_DESC
                // y se vuelve a cargar la lista de shows
                R.id.opt_ordenar_alfabetico -> {
                    vm.modificarEstadoOrdenacion(
                        if (vm.estadoOrdenacion == EstadoOrdenacion.ALFABETICO_ASC)
                            EstadoOrdenacion.ALFABETICO_DESC
                        else
                            EstadoOrdenacion.ALFABETICO_ASC
                    )
                    getShows()
                    true
                }
                // Si es PUNTUACION_ASC, se invierte a PUNTUACION_DESC
                // y se vuelve a cargar la lista de shows
                R.id.opt_ordenar_puntuacion -> {
                    vm.modificarEstadoOrdenacion(
                        if (vm.estadoOrdenacion == EstadoOrdenacion.PUNTUACION_ASC)
                            EstadoOrdenacion.PUNTUACION_DESC
                        else
                            EstadoOrdenacion.PUNTUACION_ASC
                    )
                    getShows()
                    true
                }
                else -> false
            }
        }
        getShows()
    }

    /**
     * fun onStart
     * se encarga de iniciar la actividad
     *
     * @autor Jose Pinilla
     */
    override fun onStart() {
        super.onStart()
        getShows()
        binding.swipeRefresh.setOnRefreshListener {
            getShows()
        }
    }

    /**
     * fun getShows
     * se encarga de obtener los shows
     *
     * @autor Jose Pinilla
     */
    private fun getShows() {
        adapter.submitList(emptyList())
        if(checkConnection(this)) {
            lifecycleScope.launch {
                binding.swipeRefresh.isRefreshing = true
                combine(vm.currentShow, vm.stateShows) { shows, stateShows ->
                    // Actualizamos el estado de favorito y visto de los shows
                    shows.forEach {
                        val stateShow = stateShows.find { stateShow ->
                            stateShow.idShow == it.id
                        }
                        it.favorite = stateShow?.stateFavorite ?: false
                        it.watched = stateShow?.stateWatched ?: false
                    }

                    // Ordenamos según el modo actual del ViewModel, ALFABETICO_ASC, ALFABETICO_DESC,
                    // PUNTUACION_ASC o PUNTUACION_DESC
                    val sortedList = when (vm.estadoOrdenacion) {
                        EstadoOrdenacion.ALFABETICO_ASC -> shows.sortedBy { it.name?.uppercase() ?: "" }
                        EstadoOrdenacion.ALFABETICO_DESC -> shows.sortedByDescending { it.name?.uppercase() ?: "" }
                        EstadoOrdenacion.PUNTUACION_ASC -> shows.sortedBy { it.rating?.average ?: 0.0 } // Si es null, se ordena al final
                        EstadoOrdenacion.PUNTUACION_DESC -> shows.sortedByDescending { it.rating?.average ?: 0.0 }
                    }

                    adapter.submitList(sortedList)
                    binding.swipeRefresh.isRefreshing = false
                }.catch {
                    Toast.makeText(
                        this@MainActivity, it.message, Toast.LENGTH_SHORT
                    ).show()
                }.collect()
            }
        } else {
            binding.swipeRefresh.isRefreshing = false
            Toast.makeText(this@MainActivity,
                getString(R.string.txt_noConnection),
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}
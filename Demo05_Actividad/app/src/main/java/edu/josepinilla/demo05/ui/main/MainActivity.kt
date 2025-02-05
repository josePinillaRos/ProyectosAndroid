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

    private var esAlfabeticamenteDescendente = false
    private var esPuntuacionDescendente = false

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
                R.id.opt_ordenar_alfabetico -> {
                    // Invertimos el estado
                    esAlfabeticamenteDescendente = !esAlfabeticamenteDescendente
                    lifecycleScope.launch {
                        vm.currentShow.collect { shows ->
                            val sortedList = if (esAlfabeticamenteDescendente) {
                                shows.sortedByDescending { it.name?.uppercase() ?: "" }
                            } else {
                                shows.sortedBy { it.name?.uppercase() ?: "" }
                            }
                            adapter.submitList(sortedList)

                            binding.recyclerView.scrollToPosition(shows.size - 1)
                        }
                    }
                    true
                }
                R.id.opt_ordenar_puntuacion -> {
                    // Invertimos el estado
                    esPuntuacionDescendente = !esPuntuacionDescendente
                    lifecycleScope.launch {
                        vm.currentShow.collect { shows ->
                            val sortedList = if (esPuntuacionDescendente) {
                                shows.sortedByDescending { it.rating?.average }
                            } else {
                                shows.sortedBy { it.rating?.average }
                            }
                            adapter.submitList(sortedList)

                            binding.recyclerView.scrollToPosition(shows.size - 1)
                        }
                    }
                    true

                }

                else -> false
            }
        }

        getShows()
    }

    override fun onStart() {
        super.onStart()
        getShows()
        binding.swipeRefresh.setOnRefreshListener {
            getShows()
        }

       /* binding.mToolbar.setOnClickListener { menuItem ->
            when (menuItem.id) {
                R.id.opt_ordenar_alfabetico -> {
                    lifecycleScope.launch {
                        vm.currentShow.collect {
                            adapter.submitList(it.sortedByDescending {
                                it.name!!.uppercase()
                            })
                        }
                    }
                }
                R.id.opt_ordenar_puntuacion-> {
                    lifecycleScope.launch {
                        vm.currentShow.collect {
                            adapter.submitList(it.sortedBy {
                                it.rating!!.average
                            })
                        }
                    }
                }
            }
        }*/
    }

    private fun getShows() {
        adapter.submitList(emptyList())
        if(checkConnection(this)) {
            lifecycleScope.launch {
                binding.swipeRefresh.isRefreshing = true
                combine(vm.currentShow, vm.stateShows) { shows, stateShows ->
                    shows.forEach {
                        val stateShow = stateShows.find { stateShow ->
                            stateShow.idShow == it.id
                        }
                        it.favorite = stateShow?.stateFavorite ?: false
                        it.watched = stateShow?.stateWatched ?: false
                    }
                    adapter.submitList(shows.sortedBy {
                        it.name!!.uppercase()
                    })
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
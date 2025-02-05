package edu.josepinilla.demo05.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.model.StateShows
import edu.josepinilla.demo05.model.shows.Shows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * enum class EstadoOrdenacion
 * se encarga de almacenar los estados de ordenación
 * por orden alfabetico o por puntuación y en
 * orden ascendente o descendente
 *
 * @autor Jose Pinilla
 */
enum class EstadoOrdenacion {
    ALFABETICO_ASC,
    ALFABETICO_DESC,
    PUNTUACION_ASC,
    PUNTUACION_DESC
}

/**
 * class MainViewModel
 * se encarga de manejar la información de los shows
 *
 * @param repository Repositorio de la aplicación
 *
 * @autor Jose Pinilla
 */
class MainViewModel(val repository: Repository): ViewModel() {
    private var _currentShows = repository.fetchShows()
    val currentShow: Flow<Shows>
        get() = _currentShows

    val stateShows: Flow<List<StateShows>> = repository.fetchStateShows()

    // Estado de ordenación de los shows, se inicializa por defecto a orden alfabético ascendente
    var estadoOrdenacion: EstadoOrdenacion = EstadoOrdenacion.ALFABETICO_ASC

    /**
     * fun updateStateShows
     * se encarga de actualizar el estado de los shows
     *
     * @param stateShows estado de los shows
     *
     * @autor Jose Pinilla
     */
    fun updateStateShows(stateShows: StateShows) {
        viewModelScope.launch {
            val stateShowsAux = stateShows.copy(
                stateFavorite = stateShows.stateFavorite,
                stateWatched = stateShows.stateWatched
            )
            repository.saveStateShow(stateShowsAux)
        }
    }

    /**
     * fun modificarEstadoOrdenacion
     * se encarga de modificar el estado de ordenación
     *
     * @param estado estado de ordenación
     *
     * @autor Jose Pinilla
     */
    fun modificarEstadoOrdenacion(estado: EstadoOrdenacion) {
        estadoOrdenacion = estado
    }
}

/**
 * class MainViewModelFactory
 * se encarga de manejar la información de los shows
 *
 * @param repository Repositorio de la aplicación
 * @return T ViewModel
 *
 * @autor Jose Pinilla
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}
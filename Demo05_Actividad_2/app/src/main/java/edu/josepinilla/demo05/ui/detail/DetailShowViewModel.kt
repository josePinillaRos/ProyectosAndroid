package edu.josepinilla.demo05.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.launch

/**
 * class DetailShowViewModel
 * se encarga de manejar la información de los shows
 * @param repository Repositorio de la aplicación
 * @param idShow Identificador del show
 *
 * @author Jose Pinilla
 */
class DetailShowViewModel(val repository: Repository, val idShow: Int) : ViewModel() {
    val show = repository.fetchShowsById(idShow)
    val casting = repository.fetchCastById(idShow)

    val stateShow = repository.fetchStateShowById(idShow)

    fun updateStateShows(stateShows: StateShows) {
        viewModelScope.launch {
            repository.saveStateShow(stateShows)
        }
    }
}

/**
 * class DetailShowViewModelFactory
 * se encarga de manejar la información de los shows
 * @param repository Repositorio de la aplicación
 * @param idShow Identificador del show
 * @return T ViewModel
 *
 * @autor Jose Pinilla
 */
@Suppress("UNCHECKED_CAST")
class DetailShowViewModelFactory(
    private val repository: Repository,
    private val idShow: Int
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return DetailShowViewModel(repository, idShow) as T
    }
}
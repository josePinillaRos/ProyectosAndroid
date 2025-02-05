package edu.josepinilla.demo05.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.model.StateShows
import edu.josepinilla.demo05.model.shows.Shows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository): ViewModel() {
    private var _currentShows = repository.fetchShows()
    val currentShow: Flow<Shows>
        get() = _currentShows

    val stateShows: Flow<List<StateShows>> = repository.fetchStateShows()

    fun updateStateShows(stateShows: StateShows) {
        viewModelScope.launch {
            val stateShowsAux = stateShows.copy(
                stateFavorite = stateShows.stateFavorite,
                stateWatched = stateShows.stateWatched
            )
            repository.saveStateShow(stateShowsAux)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}
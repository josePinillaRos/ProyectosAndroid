package edu.josepinilla.demo05.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.launch

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

@Suppress("UNCHECKED_CAST")
class DetailShowViewModelFactory(
    private val repository: Repository,
    private val idShow: Int
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return DetailShowViewModel(repository, idShow) as T
    }
}
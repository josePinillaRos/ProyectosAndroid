package edu.josepinilla.demo05.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.josepinilla.demo05.data.Repository

class DetailShowViewModel(repository: Repository, idShow: Int) : ViewModel() {
    val show = repository.fetchShowsById(idShow)
    val casting = repository.fetchCastById(idShow)

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
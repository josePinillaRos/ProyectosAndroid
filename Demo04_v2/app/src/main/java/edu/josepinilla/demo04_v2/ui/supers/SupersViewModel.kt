package edu.josepinilla.demo04_v2.ui.supers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo04_v2.data.SupersRepository
import edu.josepinilla.demo04_v2.model.Editorial
import edu.josepinilla.demo04_v2.model.SuperHero
import edu.josepinilla.demo04_v2.model.SupersWithEditorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupersViewModel(private val supersRepository: SupersRepository, private val superId: Int) : ViewModel() {

    private val _stateSupers = MutableStateFlow(SuperHero())
    val stateSupers: StateFlow<SuperHero> = _stateSupers.asStateFlow()

    private val _stateEditorial = MutableStateFlow(Editorial())
    val stateEditorial: StateFlow<Editorial> = _stateEditorial.asStateFlow()

    val allEditorial: Flow<List<Editorial>> = supersRepository.allEditorial

    init {
        viewModelScope.launch {
            val superAux: SupersWithEditorial? = supersRepository.getSuperById(superId)
            if(superAux != null) {
                _stateSupers.value = superAux.superHero
                _stateEditorial.value = superAux.editorial
            }
        }
    }

    fun saveSuper(superHero: SuperHero) {
        viewModelScope.launch {
            val superAux = _stateSupers.value.copy(
                superName = superHero.superName,
                realName = superHero.realName,
                favorite = superHero.favorite,
                idEditorial = superHero.idEditorial
            )
            supersRepository.insertSuperHero(superAux)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SupersViewModelFactory(private val supersRepository: SupersRepository, private val superId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SupersViewModel(supersRepository, superId) as T
    }
}
package edu.josepinilla.demo04_v2.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.demo04_v2.data.SupersRepository
import edu.josepinilla.demo04_v2.model.Editorial
import edu.josepinilla.demo04_v2.model.SupersWithEditorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val supersRepository: SupersRepository) : ViewModel() {
    val numEditorials: Flow<Int> = supersRepository.numEditorials

    val currentSuperHero = supersRepository.allSupersWithEditorial

    fun updateFavorite(supersWithEditorial: SupersWithEditorial) {
        viewModelScope.launch {
            val superAux = supersWithEditorial.superHero.copy(
                favorite = if(supersWithEditorial.superHero.favorite == 0) 1 else 0
            )
            supersRepository.insertSuperHero(superAux)
        }
    }

    fun saveEditorial(name: String) {
        val editorial = Editorial(0, name)
        viewModelScope.launch {
            supersRepository.insertEditorial(editorial)
        }
    }

    /**
     * fun deleteSuper
     * metodo seguro para recoger una función suspendida.
     * Elimina el superheroe en la ui
     *
     * @author Jose Pinilla
     */
    fun deleteSuper(supersWithEditorial: SupersWithEditorial) {
        val superAux = supersWithEditorial.superHero
        viewModelScope.launch {
            supersRepository.deleteSuperHero(superAux)
        }
    }
}
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val supersRepository: SupersRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(supersRepository) as T
    }
}
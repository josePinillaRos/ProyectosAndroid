package edu.pract5.apirestfree.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.pract5.apirestfree.data.Repository
import edu.pract5.apirestfree.model.Animals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Class MainViewModel.kt
 * ViewModel de la actividad principal.
 * @author Jose Pinilla
 *
 * @param repository Repositorio de la aplicacion.
 */
class MainViewModel(val repository: Repository): ViewModel() {

    private val _animals = MutableStateFlow<List<Animals>>(emptyList())
    val animals: StateFlow<List<Animals>> get() = _animals

    init {
        getAnimals()
    }

    /**
     * method getAnimals
     * Obtiene los animales de la API y los emite en forma de Flow.
     * @author Jose Pinilla
     *
     * @return Flow<List<Animals>> Lista de animales.
     */
    fun getAnimals() {
        viewModelScope.launch {
            repository.fetchAnimals().collect{ animalList ->
                _animals.value = animalList
            }
            Log.i("MainViewModel", "getAnimals ${_animals.value}")
        }
    }

    /**
     * method updateListAnimals
     * Obtiene los animales de la API que coincidan con el nombre y los emite en forma de Flow.
     * @author Jose Pinilla
     */
    fun updateListAnimals(query: String) {
        viewModelScope.launch {
            repository.fetchAnimalsByName(query).collect { animalsList ->
                _animals.value = animalsList
            }
            Log.i("MainViewModel", "updateListAnimals => ${_animals.value.size} animals for '$query'")
        }
    }

    val favoriteAnimals: Flow<List<Animals>> = repository.getFavoriteAnimals()

    /**
     * method updateFavoriteAnimal
     * Actualiza el estado de favorito de un animal en la base de datos local
     * introduciendolo o eliminandolo.
     * @author Jose Pinilla
     *
     * @param animal Animal a actualizar.
     */
    fun updateFavoriteAnimal(animal: Animals) {
        viewModelScope.launch {
            if (animal.favorita) {
                repository.insertAnimal(animal)
            } else {
                repository.deleteAnimal(animal)
            }
        }

    }
}

/**
 * Class MainViewModelFactory
 * Factory para crear el MainViewModel.
 * @author Jose Pinilla
 *
 * @param repository Repositorio de la aplicacion.
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}

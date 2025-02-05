package edu.actividad.demo06.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.actividad.demo06.data.Repository
import kotlinx.coroutines.launch

/**
 * MainViewModel
 * Clase que representa el ViewModel de la actividad principal
 *
 * @param repository repositorio de datos
 *
 * @author Jose Pinilla
 */
class MainViewModel(val repository: Repository) : ViewModel() {
    private var _currentCities = repository.fetchCities()
    val currentCities
        get() = _currentCities

    private var _currentVisitedCities = repository.fetchArrayCities()
    val currentVisitedCities
        get() = _currentVisitedCities

    private var _currentAllCitiesDocs = repository.fetchArrayAllCitiesDocs()
    val currentAllCitiesDocs
        get() = _currentAllCitiesDocs

    init {
        repository.createDocument()
    }

    /**
     * addCity
     * Método que agrega una ciudad a Firebase
     *
     * @param city nombre de la ciudad
     * @param countryCode código del país
     *
     * @author Jose Pinilla
     */
    fun addCity(city: String, countryCode: String) {
        repository.addCity(city, countryCode)
    }

    /**
     * updateListCities
     * Método que actualiza la lista de ciudades
     *
     * @param query consulta de búsqueda
     *
     * @author Jose Pinilla
     */
    fun updateListCities(query: String) {
        viewModelScope.launch {
            _currentCities = if (query.isNotBlank())
                repository.fetchCitiesByName(query)
            else repository.fetchCities()
        }
    }
}

/**
 * MainViewModelFactory
 * clase que representa el Factory del ViewModel de la actividad principal
 *
 * @param repository repositorio de datos
 *
 * @autor Jose Pinilla
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}
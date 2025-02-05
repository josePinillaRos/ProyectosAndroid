package edu.actividad.demo06.data

import edu.actividad.demo06.model.City

/**
 * RemoteDataSource
 * Clase que representa el origen de datos remoto
 *
 * @author Jose Pinilla
 */
class RemoteDataSource {
    private val api = CitiesAPI.getRetrofit2Api()
    suspend fun getCities(): List<City> {
        return api.getCities()
    }
    suspend fun getCitiesByName(name: String): List<City> {
        return api.getCitiesByName(name)
    }
}
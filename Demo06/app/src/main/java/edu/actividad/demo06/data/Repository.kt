package edu.actividad.demo06.data

import android.util.Log
import edu.actividad.demo06.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class Repository(db: CitiesRoomDB, val ds: RemoteDataSource) {
    val TAG = Repository::class.java.simpleName
    private val localDataSource = LocalDataSource(db.citiesDao())

    private val fbRepository = RepositoryFirebase()

    fun createDocument() {
        fbRepository.createDocument()
    }

    fun addCity(city: String, countryCode: String) {
        fbRepository.addCity(city, countryCode)
    }

    fun fetchArrayCities(): Flow<List<Map<String, String>>> = runBlocking {
        fbRepository.fetchArrayCities()
    }

    fun fetchCities(): Flow<List<City>> {
        return flow {
            var resultDB = emptyList<City>()
            try {
                // Se intenta recuperar la información de la base de datos.
                resultDB = localDataSource.getCities()
                // Se intenta recuperar la información de la API.
                val resultAPI = ds.getCities()
                // Se compara la información de la API y la de la base de datos.
                if (resultDB.containsAll(resultAPI)) {
                    // Se emite el resultado.
                    emit(resultDB)

                } else {
                    // Se inserta la información en la base de datos.
                    localDataSource.insertCities(resultAPI)

                }
                // Se recupera la información de la base de datos actualizada.
                resultDB = localDataSource.getCities()
            } catch (e: Exception) {
                // Se emite el error.
                Log.e(TAG, "fetchCities: ${e.message}")
            } finally {
                // Se emite el resultado, ya sea de la base de datos o de la API.
                // Una lista con datos o vacía.
                emit(resultDB)
            }
        }
    }
    fun fetchCitiesByName(name: String): Flow<List<City>> {
        return flow {
            var resultDB = emptyList<City>()
            try {
                // Se intenta recuperar la información de la base de datos.
                resultDB = localDataSource.getCitiesByName("%$name%")
                // Se intenta recuperar la información de la API.
                val resultAPI = ds.getCitiesByName(name)
                // Se compara la información de la API y la de la base de datos.
                if (resultDB.containsAll(resultAPI)) {
                    // Se emite el resultado.
                    emit(resultDB)

                } else {
                    // Se inserta la información en la base de datos.
                    localDataSource.insertCities(resultAPI)

                }
                // Se recupera la información de la base de datos actualizada.
                resultDB = localDataSource.getCitiesByName("%$name%")
            } catch (e: Exception) {
                // Se emite el error.
                Log.e(TAG, "fetchCitiesByName: ${e.message}")
            } finally {
                // Se emite el resultado, ya sea de la base de datos o de la API.
                // Una lista con datos o vacía.
                emit(resultDB)
            }
        }
    }
}

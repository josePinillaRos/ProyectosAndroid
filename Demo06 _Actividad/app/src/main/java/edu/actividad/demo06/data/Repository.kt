package edu.actividad.demo06.data

import android.util.Log
import edu.actividad.demo06.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * Repository
 * Clase que representa el repositorio de datos
 *
 * @param db base de datos de Room
 * @param ds origen de datos remoto
 *
 * @author Jose Pinilla
 */
class Repository(db: CitiesRoomDB, val ds: RemoteDataSource) {
    val TAG = Repository::class.java.simpleName
    private val localDataSource = LocalDataSource(db.citiesDao())

    private val fbRepository = RepositoryFirebase()

    /**
     * createDocument
     * Método que crea un documento en Firebase
     *
     * @author Jose Pinilla
     */
    fun createDocument() {
        fbRepository.createDocument()
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
        fbRepository.addCity(city, countryCode)
    }

    /**
     * fetchArrayCities
     * Método que obtiene un flujo de ciudades de Firebase
     *
     * @return Flow<List<Map<String, String>>> flujo de ciudades
     *
     * @author Jose Pinilla
     */
    fun fetchArrayCities(): Flow<List<Map<String, String>>> = runBlocking {
        fbRepository.fetchArrayCities()
    }

    /**
     * fetchCities
     * Método que obtiene un flujo de ciudades
     *
     * @return Flow<List<City>> flujo de ciudades
     *
     * @autor Jose Pinilla
     */
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

    /**
     * fetchCitiesByName
     * Método que obtiene un flujo de ciudades por nombre
     *
     * @param name nombre de la ciudad
     *
     * @return Flow<List<City>> flujo de ciudades
     *
     * @author Jose Pinilla
     */
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

    /**
     * fetchArrayAllCitiesDocs
     * Método que obtiene un flujo de ciudades agrupadas por nombre y countryCode
     *
     * @return Flow<List<Map<Map<String, String>, Int>> flujo de ciudades
     *
     * @autor Jose Pinilla
     */
    fun fetchArrayAllCitiesDocs(): Flow<List<Map<Map<String, String>, Int>>> {
        return fbRepository.fetchArrayAllCitiesDocs()
    }
}

package edu.pract5.apirestfree.data

import android.util.Log
import edu.pract5.apirestfree.model.Animals
import edu.pract5.apirestfree.model.AnimalsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Class Repository.kt
 * Se encarga de la logica de negocio de la aplicacion.
 * @author Jose Pinilla
 *
 * @param remoteDataSource Fuente de datos remota.
 * @param localDataSource Fuente de datos local.
 */
class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    // Metodos de la API

    /**
     * method fetchAnimals
     * Obtiene los animales de la API y los emite en forma de Flow.
     * @author Jose Pinilla
     *
     * @return Flow<List<Animals>> Lista de animales.
     */
    fun fetchAnimals(): Flow<List<Animals>> = flow {
        val firstAnimals = remoteDataSource.getFirstAnimals()
        Log.i("Repository", "fetchAnimals {$firstAnimals}")
        emit(firstAnimals)
    }

    /**
     * method fetchAnimalsByName
     * Obtiene los animales de la API que coincidan con el nombre y los emite en forma de Flow.
     * @author Jose Pinilla
     *
     * @param name Nombre del animal a buscar.
     * @return Flow<List<Animals>> Lista de animales.
     */
    fun fetchAnimalsByName(name: String): Flow<List<Animals>> = flow {
        try {
            val animalsAPI = remoteDataSource.getAnimalsByName(name)
            emit(animalsAPI)
        } catch (e: Exception) {
            Log.e("Repository", "fetchAnimalsByName: ${e.message}")
        } finally {
            Log.i("Repository", "fetchAnimalsByName: finally")
        }
    }

    // Metodos de la base de datos local

    /**
     * method mapAnimalsToEntity
     * Convierte un Animals en AnimalsEntity, usando CSV para `locationsStr`
     * Así se puede guaradar en la bbdd el array de locations.
     * @author Jose Pinilla
     *
     * @param animal Animals a convertir.
     * @return AnimalsEntity convertido.
     */
    private fun mapAnimalsToEntity(animal: Animals): AnimalsEntity {
        val csvLocations = animal.locations?.joinToString(separator = ",") { it ?: "" } ?: ""
        return AnimalsEntity(
            name = animal.name,
            locationsStr = csvLocations,
            favorita = animal.favorita,
            characteristics = animal.characteristics,
            taxonomy = animal.taxonomy,
        )
    }

    /**
     * method mapEntityToAnimals
     * Convierte un AnimalsEntity en Animals, así se puede extraer
     * la información de la base de datos local.
     * @author Jose Pinilla
     *
     * @param entity AnimalsEntity a convertir.
     * @return Animals convertido.
     */
    private fun mapEntityToAnimals(entity: AnimalsEntity): Animals {
        val locationsList = entity.locationsStr
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()

        return Animals(
            characteristics = entity.characteristics,
            locations = locationsList,
            name = entity.name,
            taxonomy = entity.taxonomy,
            favorita = entity.favorita
        )
    }

    /**
     * method insertAnimal
     * Guarda un animal en la bbdd.
     * @author Jose Pinilla
     *
     * @param animal Animal a guardar.
     */
    suspend fun insertAnimal(animal: Animals) {
        val entity = mapAnimalsToEntity(animal)
        localDataSource.insertAnimal(entity)
    }

    /**
     * method getFavoriteAnimals
     * Devuelve todos los animales de la BD local en forma de Animals.
     * @author Jose Pinilla
     *
     * @return Flow<List<Animals>> Lista de animales favoritos.
     */
    fun getFavoriteAnimals(): Flow<List<Animals>> {
        return localDataSource.getAllAnimals().map { listEntities ->
            listEntities.map { entity -> mapEntityToAnimals(entity) }
        }
    }

    /**
     * method deleteAnimal
     * Elimina de la BD un animal.
     * @author Jose Pinilla
     *
     * @param animal Animal a eliminar.
     */
    suspend fun deleteAnimal(animal: Animals) {
        val entityInDb = localDataSource.getAnimalByName(animal.name)
        if (entityInDb != null) {
            localDataSource.deleteAnimal(entityInDb)
        }
    }
}
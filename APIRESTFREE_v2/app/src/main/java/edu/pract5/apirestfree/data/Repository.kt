package edu.pract5.apirestfree.data

import android.util.Log
import edu.pract5.apirestfree.model.Animals
import edu.pract5.apirestfree.model.AnimalsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    // ==================================================
    // MÉTODOS PARA LA API (Remoto)
    // ==================================================

    fun fetchAnimals(): Flow<List<Animals>> = flow {
        val firstAnimals = remoteDataSource.getFirstAnimals()
        Log.i("Repository", "fetchAnimals {$firstAnimals}")
        emit(firstAnimals)
    }

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

    // ==================================================
    // MÉTODOS PARA LA BASE DE DATOS (Local)
    // ==================================================

    /**
     * Convierte un [Animals] en [AnimalsEntity], usando CSV para `locationsStr`.
     */
    private fun mapAnimalsToEntity(animal: Animals): AnimalsEntity {
        val csvLocations = animal.locations?.joinToString(separator = ",") { it ?: "" } ?: ""
        return AnimalsEntity(
            // id se autogenera en la BD
            name = animal.name,
            locationsStr = csvLocations,
            favorita = animal.favorita,
            characteristics = animal.characteristics,
            taxonomy = animal.taxonomy,
        )
    }

    /**
     * Convierte un [AnimalsEntity] en [Animals].
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
     * Guarda (inserta o reemplaza) una lista de animales en la base de datos local.
     */
    suspend fun insertAnimal(animal: Animals) {
        val entity = mapAnimalsToEntity(animal)
        localDataSource.insertAnimal(entity)
    }

    /**
     * Devuelve todos los animales de la BD local en forma de [Animals].
     */
    fun getFavoriteAnimals(): Flow<List<Animals>> {
        return localDataSource.getAllAnimals().map { listEntities ->
            listEntities.map { entity -> mapEntityToAnimals(entity) }
        }
    }

    /**
     * Elimina de la BD el primer animal que coincida por 'name'.
     * (Asume que 'name' es un identificador válido.)
     */
    suspend fun deleteAnimal(animal: Animals) {
        val entityInDb = localDataSource.getAnimalByName(animal.name)
        if (entityInDb != null) {
            localDataSource.deleteAnimal(entityInDb)
        }
    }
}
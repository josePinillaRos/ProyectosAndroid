package edu.pract5.apirestfree.data

import edu.pract5.apirestfree.model.AnimalsEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource (val db: AnimalsDao) {

    fun getAllAnimals() : Flow<List<AnimalsEntity>> {
        return db.getAllAnimals()
    }

    suspend fun insertAnimal(animal: AnimalsEntity){
        db.insertAnimal(animal)
    }

    suspend fun deleteAnimal(animal: AnimalsEntity){
        db.deleteAnimal(animal)
    }

    suspend fun getAnimalByName(name: String?): AnimalsEntity? {
        return db.getAnimalByName(name)
    }
}
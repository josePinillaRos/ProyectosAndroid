package edu.pract5.apirestfree.data

import android.util.Log
import edu.pract5.apirestfree.model.Animals

class RemoteDataSource {
    private val api = AnimalsAPI.getRetrofit2Api()

    suspend fun getFirstAnimals(): List<Animals> {
        Log.i("RemoteDataSource", "getFirstAnimals {${api.getFirstAnimals()}}")
        return api.getFirstAnimals()
    }

    suspend fun getAnimalsByName(name: String): List<Animals> {
        return api.getAnimalsByName(name)
    }
}
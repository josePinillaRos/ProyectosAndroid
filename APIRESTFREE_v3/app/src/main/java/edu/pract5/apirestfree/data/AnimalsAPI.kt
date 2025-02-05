package edu.pract5.apirestfree.data

import edu.pract5.apirestfree.BuildConfig
import edu.pract5.apirestfree.model.Animals
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Class AnimalsAPI.kt
 * Se encarga de recoger los datos de la API.
 * @author Jose Pinilla
 */
class AnimalsAPI {
    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/"
        fun getRetrofit2Api(): AnimalsAPIInterface {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(AnimalsAPIInterface::class.java)
        }
    }
}

/**
 * Interface AnimalsAPIInterface.kt
 * Metodos para llamar a la api mediante sus endpoints.
 * @author Jose Pinilla
 */
interface AnimalsAPIInterface {

    @Headers("X-Api-Key: ${BuildConfig.API_KEY}")
    @GET("v1/animals")
    suspend fun getFirstAnimals(
        @Query("name") name: String = "Aa"
    ): List<Animals>

    @Headers("X-Api-Key: ${BuildConfig.API_KEY}")
    @GET("v1/animals")
    suspend fun getAnimalsByName(
        @Query("name") name: String
    ): List<Animals>
}
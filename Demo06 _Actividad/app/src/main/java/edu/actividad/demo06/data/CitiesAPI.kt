package edu.actividad.demo06.data

import edu.actividad.demo06.BuildConfig
import edu.actividad.demo06.model.City
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * class CitiesAPI
 * llamada a la API de ciudades de apis ninjas
 *
 * @author Jose Pinilla
 */
class CitiesAPI {
    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/"
        fun getRetrofit2Api(): CitiesAPIInterface {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(CitiesAPIInterface::class.java)
        }
    }
}

/**
 * interface CitiesAPIInterface
 * metodos para recolectar ciudades con las anotaciones de retrofit
 *
 * @author Jose Pinilla
 */
interface CitiesAPIInterface {
    @Headers("X-Api-Key: ${BuildConfig.API_KEY}")
    @GET("v1/city")
    /**
     * getCities
     * metodo para obtener ciudades
     *
     * @return List<City> lista de ciudades
     *
     * @author Jose Pinilla
     */
    suspend fun getCities(
        @Query("min_population") min_population: Int = 1,
        @Query("limit") limit: Int = 30
    ): List<City>

    @Headers("X-Api-Key: ${BuildConfig.API_KEY}")
    @GET("v1/city")
    /**
     * getCitiesByName
     * metodo para obtener cities buscando por su nombre
     *
     * @return List<City> lista de ciudades
     *
     * @author Jose Pinilla
     */
    suspend fun getCitiesByName(
        @Query("name") name: String,
        @Query("limit") limit: Int = 30
    ): List<City>
}
package edu.josepinilla.demo05.data

import edu.josepinilla.demo05.model.cast.Cast
import edu.josepinilla.demo05.model.shows.Shows
import edu.josepinilla.demo05.model.shows.ShowsItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * class Retrofit2Api
 * se encarga de obtener la instancia de la API
 *
 * @property BASE_URL url base de la API
 *
 * @author Jose Pinilla
 */
class Retrofit2Api {
    companion object {
        const val BASE_URL = "https://api.tvmaze.com/"

        /**
         * fun getRetrofit2Api
         * obtiene la instancia de la API
         *
         * @return instancia de la API
         *
         * @author Jose Pinilla
         */
        fun getRetrofit2Api(): Retrofit2ApiInterface {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(Retrofit2ApiInterface::class.java)
        }
    }
}

/**
 * interface Retrofit2ApiInterface
 * interfaz de la API
 *
 * @return Shows
 * @return ShowsItem
 * @return Cast
 *
 * @author Jose Pinilla
 */
interface Retrofit2ApiInterface {
    @GET("shows")
    suspend fun getShows() : Shows

    @GET("shows/{id}")
    suspend fun getShowById(@Path("id") id: Int) : ShowsItem

    @GET("shows/{id}/cast")
    suspend fun getCastByShowId(@Path("id") id: Int) : Cast
}
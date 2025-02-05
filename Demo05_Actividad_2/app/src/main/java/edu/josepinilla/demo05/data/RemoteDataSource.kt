package edu.josepinilla.demo05.data

import android.util.Log
import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * class RemoteDataSource
 * se encarga de obtener los datos de la API y de la base de datos
 *
 * @author Jose Pinilla
 */
class RemoteDataSource (val db: StateShowsDao) {
    private val api = Retrofit2Api.getRetrofit2Api()

    /**
     * fun getShows
     * obtiene los shows de la API
     *
     * @return flow con los shows
     *
     * @author Jose Pinilla
     */
    fun getShows() = flow {
        emit(api.getShows())
    }

    /**
     * fun getShowById
     * obtiene un show por su id
     *
     * @param id id del show
     *
     * @return flow con el show
     *
     * @author Jose Pinilla
     */
    fun getShowById(id: Int) = flow {
        emit(api.getShowById(id))
    }

    /**
     * fun getCastByShowId
     * obtiene el casting de un show por su id
     *
     * @param id id del show
     *
     * @return flow con el casting
     *
     * @author Jose Pinilla
     */
    fun getCastByShowId(id: Int) = flow {
        emit(api.getCastByShowId(id))
    }

    /**
     * fun insertStateShow
     * inserta el estado de un show en la base de datos
     *
     * @param stateShow estado del show
     *
     * @author Jose Pinilla
     */
    suspend fun insertStateShow(stateShow: StateShows) {
        Log.i("RemoteDataSource", "insertStateShow")
        db.insertStateShow(stateShow)
    }

    /**
     * fun updateStateShow
     * actualiza el estado de un show en la base de datos
     *
     * @param stateShow estado del show
     *
     * @return flow con el estado del show
     *
     * @author Jose Pinilla
     */
    fun getAllStateShows(): Flow<List<StateShows>> {
        return db.getAllStateShows()
    }

    /**
     * fun updateStateShow
     * actualiza el estado de un show en la base de datos
     *
     * @param stateShow estado del show
     *
     * @return flow con el estado del show
     *
     * @author Jose Pinilla
     */
    fun getStateShowById(id: Int): Flow<StateShows?> {
        return db.getStateShowById(id)
    }
}
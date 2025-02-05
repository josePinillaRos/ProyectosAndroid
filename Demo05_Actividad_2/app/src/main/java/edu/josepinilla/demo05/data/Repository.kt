package edu.josepinilla.demo05.data

import android.util.Log
import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

/**
 * class Repository
 * se encarga de obtener los datos de la API y de la base de datos
 *
 * @param dataSource fuente de RemoteDataSource
 *
 * @author Jose Pinilla
 */
class Repository(val dataSource: RemoteDataSource) {
    fun fetchShows() = dataSource.getShows()
    fun fetchShowsById(id: Int) = dataSource.getShowById(id)
    fun fetchCastById(id: Int) =  dataSource.getCastByShowId(id)

    fun saveStateShow(stateShow: StateShows) = runBlocking {
        Log.i("Repository", "saveStateShow")
        dataSource.insertStateShow(stateShow)
        delay(10)
    }
    fun fetchStateShows(): Flow<List<StateShows>> {
        return dataSource.getAllStateShows()
    }

    fun fetchStateShowById(idShow: Int): Flow<StateShows?> {
        return dataSource.getStateShowById(idShow)
    }
}
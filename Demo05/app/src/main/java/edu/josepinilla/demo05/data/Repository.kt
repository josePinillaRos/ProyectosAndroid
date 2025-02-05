package edu.josepinilla.demo05.data

import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class Repository(val dataSource: RemoteDataSource) {
    fun fetchShows() = dataSource.getShows()
    fun fetchShowsById(id: Int) = dataSource.getShowById(id)
    fun fetchCastById(id: Int) =  dataSource.getCastByShowId(id)

    suspend fun saveStateShow(stateShow: StateShows) = runBlocking {
        dataSource.insertStateShow(stateShow)
        delay(10)
    }
    fun fetchStateShows(): Flow<List<StateShows>> {
        return dataSource.getAllStateShows()
    }
    suspend fun getStateShowByIdShow(idShow: Int): StateShows? {
        return dataSource.getStateShowByIdShow(idShow)
    }
}
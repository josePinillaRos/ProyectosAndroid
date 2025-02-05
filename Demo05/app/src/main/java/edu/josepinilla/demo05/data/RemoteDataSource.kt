package edu.josepinilla.demo05.data

import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource (val db: StateShowsDao) {
    private val api = Retrofit2Api.getRetrofit2Api()


    fun getShows() = flow {
        emit(api.getShows())
    }

    fun getShowById(id: Int) = flow {
        emit(api.getShowById(id))
    }

    fun getCastByShowId(id: Int) = flow {
        emit(api.getCastByShowId(id))
    }

    suspend fun insertStateShow(stateShow: StateShows) {
        db.insertStateShow(stateShow)
    }
    fun getAllStateShows(): Flow<List<StateShows>> {
        return db.getAllStateShows()
    }
    suspend fun getStateShowByIdShow(id: Int) : StateShows? {
        return db.getStateShowsByIdShow(id)
    }
}
package edu.josepinilla.demo05.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import edu.josepinilla.demo05.model.StateShows
import kotlinx.coroutines.flow.Flow

/**
 * abstract class StateShowsDatabase
 * se encarga de la base de datos de Room
 *
 * @author Jose Pinilla
 */
@Database(entities = [StateShows::class], version = 1)
abstract class StateShowsDatabase : RoomDatabase() {
    abstract fun stateShowsDao(): StateShowsDao
}

/**
 * @Dao
 * interface StateShowsDao
 * se encarga de las consultas a la base de datos
 *
 * @return insertStateShow
 * @return getAllStateShows
 * @return getStateShowsByIdShow
 * @return getStateShowById
 *
 * @author Jose Pinilla
 */
@Dao
interface StateShowsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStateShow(stateShows: StateShows)
    @Query("SELECT * FROM StateShows")
    fun getAllStateShows(): Flow<List<StateShows>>
    @Query("SELECT * FROM StateShows WHERE idShow = :idShow")
    suspend fun getStateShowsByIdShow(idShow: Int): StateShows?
    @Query("SELECT * FROM StateShows WHERE idShow = :idShow LIMIT 1")
    fun getStateShowById(idShow: Int): Flow<StateShows?>
}
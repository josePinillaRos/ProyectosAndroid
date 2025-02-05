package edu.josepinilla.demo04_v2.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import edu.josepinilla.demo04_v2.model.Editorial
import edu.josepinilla.demo04_v2.model.SuperHero
import edu.josepinilla.demo04_v2.model.SupersWithEditorial
import kotlinx.coroutines.flow.Flow

@Database(entities = [SuperHero::class, Editorial::class], version = 1)
abstract class SupersDatabase : RoomDatabase() {
    abstract fun supersDao(): SupersDao
}
@Dao
interface SupersDao {

    //Funcion suspendida para eliminar Super heroe
    @Delete
    suspend fun deleteSuperHero(superHero: SuperHero)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorial(editorial: Editorial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuperHero(superHero: SuperHero)

    @Query("SELECT * FROM Editorial")
    fun getAllEditorials(): Flow<List<Editorial>>

    @Query("SELECT * FROM Editorial WHERE idEd = :editorialId")
    suspend fun getEditorialById(editorialId: Int): Editorial?

    @Query("SELECT count(idEd) FROM Editorial")
    fun getNumEditorials(): Flow<Int>


    @Query("SELECT * FROM SuperHero")
    fun getAllSuperHeroes(): Flow<List<SuperHero>>

    @Query("SELECT * FROM SuperHero WHERE idSuper = :idSuper")
    suspend fun getSuperById(idSuper: Int): SupersWithEditorial?

    @Transaction
    @Query("SELECT * FROM SuperHero ORDER BY superName")
    fun getSuperHeroesWithEditorial(): Flow<List<SupersWithEditorial>>
}
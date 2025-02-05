package edu.pract5.apirestfree.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import edu.pract5.apirestfree.model.AnimalsEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [AnimalsEntity::class], version = 1)
abstract class AnimalsDatabase : RoomDatabase() {
    abstract fun animalsDao(): AnimalsDao
}

@Dao
interface AnimalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal: AnimalsEntity)

    @Query("SELECT * FROM animals")
    fun getAllAnimals(): Flow<List<AnimalsEntity>>

    @Delete
    suspend fun deleteAnimal(animal: AnimalsEntity)

    @Query("SELECT * FROM animals WHERE name = :name LIMIT 1")
    suspend fun getAnimalByName(name: String?): AnimalsEntity?
}

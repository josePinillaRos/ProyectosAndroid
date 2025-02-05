package edu.josepinilla.demo04.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.josepinilla.demo04.model.Editorial
import edu.josepinilla.demo04.model.SuperHero

@Database(entities = [SuperHero::class, Editorial::class], version = 1)
abstract class SupersDatabase: RoomDatabase() {
    abstract fun supersDao(): SupersDao
}

@Dao
interface SupersDao {

}

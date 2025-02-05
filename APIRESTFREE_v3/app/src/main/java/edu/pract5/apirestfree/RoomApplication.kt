package edu.pract5.apirestfree

import android.app.Application
import androidx.room.Room
import edu.pract5.apirestfree.data.AnimalsDatabase

/**
 * Class RoomApplication.kt
 * Clase que inicializa la base de datos de Room.
 * @author Jose Pinilla
 */
class RoomApplication : Application() {
    lateinit var animalsDatabase: AnimalsDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        animalsDatabase = Room.databaseBuilder(
            this,
            AnimalsDatabase::class.java,
            "words_database"
        ).build()
    }
}
package edu.josepinilla.demo05

import android.app.Application
import androidx.room.Room
import edu.josepinilla.demo05.data.StateShowsDatabase

/**
 * class RoomApplication
 * se encarga de inicializar la base de datos
 * de la aplicación
 *
 * @author Jose Pinilla
 */
class RoomApplication: Application() {
    lateinit var stateShowsDB: StateShowsDatabase
        private set

    /**
     * fun onCreate
     * se encarga de inicializar la base de datos
     *
     * @author Jose Pinilla
     */
    override fun onCreate() {
        super.onCreate()
        stateShowsDB = Room.databaseBuilder(
            this,
            StateShowsDatabase::class.java, "StateShows-db"
        ).build()
    }
}

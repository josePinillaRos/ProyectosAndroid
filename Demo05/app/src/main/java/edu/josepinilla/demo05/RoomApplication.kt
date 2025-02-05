package edu.josepinilla.demo05

import android.app.Application
import androidx.room.Room
import edu.josepinilla.demo05.data.StateShowsDatabase

class RoomApplication: Application() {
    lateinit var stateShowsDB: StateShowsDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        stateShowsDB = Room.databaseBuilder(
            this,
            StateShowsDatabase::class.java, "StateShows-db"
        ).build()
    }
}

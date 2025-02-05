package edu.josepinilla.demo04_v2

import android.app.Application
import androidx.room.Room
import edu.josepinilla.demo04_v2.data.SupersDatabase

class MyRoomApplication : Application() {
    lateinit var supersDatabase: SupersDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        supersDatabase = Room.databaseBuilder(this,
            SupersDatabase::class.java, "supers-db").build()
    }
}
package edu.josepinilla.notespmdm

import android.app.Application
import androidx.room.Room
import edu.josepinilla.notespmdm.data.NotesDatabase

class NotesPMDMApplication: Application() {
    lateinit var notesDatabase: NotesDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        notesDatabase = Room.databaseBuilder(this,
            NotesDatabase::class.java, "notes-db").build()
    }
}
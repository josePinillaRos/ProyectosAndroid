package edu.josepinilla.notespmdm.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import edu.josepinilla.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * abstract class NotesDatabase : RoomDatabase()
 *
 * Clase abstracta que extiende de RoomDatabase
 * genera la base de datos de la aplicacion
 *
 * @author Jose Pinilla
 */
@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase(){
    abstract fun notesDao(): NotesDao
}

/**
 * interface NotesDao
 * metodos para la base de datos
 *
 * @author Jose Pinilla
 */
@Dao
interface NotesDao {
    //Funcion suspendida para eliminar Nota
    @Delete
    suspend fun deleteNote(note: Note) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: Note) : Long

    @Query("SELECT * FROM Note")
    fun getCurrentNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note ORDER BY title ASC")
    fun getCurrentSortedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE idNote = :noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Query("SELECT * FROM Note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT count(idNote) FROM Note")
    fun getNumNotes(): Flow<Int>
}
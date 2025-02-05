package edu.josepinilla.notespmdm.data

import edu.josepinilla.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * class NotesDataSource
 *
 * Clase que se encarga de manejar los datos de la aplicacion
 *
 * @param db llamada a la base de datos
 *
 * @author Jose Pinilla
 */
class NotesDataSource (val db: NotesDao){
    val currentNotes: Flow<List<Note>> = db.getCurrentNotes()
    val currentSortedNotes: Flow<List<Note>> = db.getCurrentSortedNotes()

    val allNotes: Flow<List<Note>> = db.getAllNotes()
    val numNotes: Flow<Int> = db.getNumNotes()

    /**
     * funcion deleteNote
     * elimina una nota
     *
     * @param note Nota a eliminar
     *
     * @return Unit
     *
     * @author Jose Pinilla
     */
    suspend fun deleteNote(note: Note){
        db.deleteNote(note)
    }

    /**
     * funcion saveNote
     * guarda una nota
     *
     * @param note Nota a guardar
     *
     * @return Unit
     *
     * @author Jose Pinilla
     */
    suspend fun saveNote(note: Note){
        db.saveNote(note)
    }

    /**
     * funcion getNoteById
     * obtiene una nota por su id
     *
     * @param noteId id de la nota
     *
     * @return Note
     *
     * @author Jose Pinilla
     */
    suspend fun getNoteById(noteId: Long): Note? = db.getNoteById(noteId)

}
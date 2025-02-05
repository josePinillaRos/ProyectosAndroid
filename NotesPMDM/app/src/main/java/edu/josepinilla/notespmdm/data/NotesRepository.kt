package edu.josepinilla.notespmdm.data

import edu.josepinilla.notespmdm.model.Note

/**
 * class NotesRepository
 *
 * Clase que se encarga de manejar los datos de la aplicacion
 *
 * @param dataSource llamada a la fuente de datos
 *
 * @author Jose Pinilla
 */
class NotesRepository (val dataSource: NotesDataSource){
    val currentNotes = dataSource.currentNotes
    val currentSortedNotes = dataSource.currentSortedNotes

    val allNotes = dataSource.allNotes
    val numNotes = dataSource.numNotes

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
    suspend fun deleteNote (note: Note) {
        dataSource.deleteNote(note)
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
    suspend fun saveNote (note: Note){
        dataSource.saveNote(note)
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
    suspend fun getNoteById (noteId: Long): Note? = dataSource.getNoteById(noteId)
}
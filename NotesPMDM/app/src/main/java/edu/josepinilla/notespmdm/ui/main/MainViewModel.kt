package edu.josepinilla.notespmdm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.notespmdm.data.NotesRepository
import edu.josepinilla.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * class MainViewModel
 * controla la vista principal de la aplicacion
 *
 * @param notesRepository repositorio de notas
 *
 * @return Unit
 *
 * @author Jose Pinilla
 */
class MainViewModel(private val notesRepository: NotesRepository): ViewModel() {
    val currentNotes: Flow<List<Note>> = notesRepository.currentNotes
    val currentSortedNotes: Flow<List<Note>> = notesRepository.currentSortedNotes

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
    fun deleteNote (note: Note) {
        val noteAux = note
        viewModelScope.launch {
            notesRepository.deleteNote(noteAux)
        }
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
    fun saveNote (note: Note) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
        }
    }
}

/**
 * class MainViewModelFactory
 * Clase que se encarga de manejar los datos de la aplicacion
 *
 * @param notesRepository llamada al repositorio de notas
 *
 * @return Unit
 *
 * @author Jose Pinilla
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val notesRepository: NotesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(notesRepository) as T
    }
}
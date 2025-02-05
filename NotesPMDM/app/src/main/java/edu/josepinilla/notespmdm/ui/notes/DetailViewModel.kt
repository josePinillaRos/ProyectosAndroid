package edu.josepinilla.notespmdm.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.josepinilla.notespmdm.data.NotesRepository
import edu.josepinilla.notespmdm.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Date

/**
 * class DetailNoteViewModel
 * controla la vista de detalle de la nota
 *
 * @param notesRepository repositorio de notas
 * @param noteId id de la nota
 *
 * @return Unit
 *
 * @autor Jose Pinilla
 */
class DetailNoteViewModel (private val  notesRepository: NotesRepository, val noteId: Long) : ViewModel() {
    private val _stateNotes = MutableStateFlow(Note())
    val stateNotes : StateFlow<Note> = _stateNotes.asStateFlow()


    init {
        viewModelScope.launch {
            val noteAux: Note? = notesRepository.getNoteById(noteId)
            if (noteAux != null) {
                _stateNotes.value = noteAux
            }
        }
    }

    /**
     * funcion deleteNote
     * elimina una nota
     *
     * @param note Nota a eliminar
     *
     * @return Unit
     *
     * @autor Jose Pinilla
     */
    fun saveNote (note: Note) {
        viewModelScope.launch {

            val noteAux = _stateNotes.value.copy(
                title = note.title,
                description = note.description,
                date = Date(System.currentTimeMillis()).toString() + " " +
                        java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date())
            )
            notesRepository.saveNote(noteAux)
        }
    }
}

/**
 * class DetailNoteViewModelFactory
 *
 * @param notesRepository repositorio de notas
 * @param noteId id de la nota
 *
 * @return T ViewModel
 *
 * @autor Jose Pinilla
 */
@Suppress("UNCHECKED_CAST")
class DetailNoteViewModelFactory(private val notesRepository: NotesRepository, private val noteId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailNoteViewModel(notesRepository, noteId) as T
    }
}
package edu.josepinilla.notespmdm.ui.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import edu.josepinilla.notespmdm.NotesPMDMApplication
import edu.josepinilla.notespmdm.R
import edu.josepinilla.notespmdm.data.NotesDataSource
import edu.josepinilla.notespmdm.data.NotesRepository
import edu.josepinilla.notespmdm.databinding.ActivityDetailNoteBinding
import edu.josepinilla.notespmdm.model.Note
import kotlinx.coroutines.launch

/**
 * Class DetailActivity
 * Actividad de detalle de la nota
 *
 *
 * @author Jose Pinilla
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNoteBinding

    private val vm: DetailNoteViewModel by viewModels {
        val db = (application as NotesPMDMApplication).notesDatabase
        val dataSource = NotesDataSource(db.notesDao())
        val repository = NotesRepository(dataSource)
        val noteIdAux= intent.getLongExtra(NOTE_ID, 0)
        DetailNoteViewModelFactory(repository, noteIdAux)
    }

    companion object {
        const val NOTE_ID = "note_id"
        fun navigate(activity: Activity, noteId: Long = 0) {
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra(NOTE_ID, noteId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(intent)
        }
    }

    /**
     * funcion onCreate
     * crea la actividad con todos sus elementos y funcionalidades
     *
     * @param savedInstanceState estado de la actividad
     *
     * @return Unit
     *
     * @autor Jose Pinilla
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mToolbar.inflateMenu(R.menu.save)
        binding.mToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.op_save_note -> {
                    val title = binding.etTitle.text.toString().trim()
                    if(title.isBlank()) {
                        binding.etTitle.error = getString(R.string.txt_errorTitle)
                    } else {
                        vm.saveNote(
                            Note(
                                idNote = vm.noteId,
                                title,
                                description = binding.etDescription.text.toString().trim()
                            )
                        )
                        finish()
                    }
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.stateNotes.collect {
                    binding.etTitle.setText(it.title)
                    binding.etDescription.setText(it.description)
                }
            }
        }
    }
}
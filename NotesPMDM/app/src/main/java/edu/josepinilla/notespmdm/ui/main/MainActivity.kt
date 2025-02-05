package edu.josepinilla.notespmdm.ui.main

import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.notespmdm.NotesPMDMApplication
import edu.josepinilla.notespmdm.R
import edu.josepinilla.notespmdm.adapters.NotesAdapter
import edu.josepinilla.notespmdm.data.NotesDataSource
import edu.josepinilla.notespmdm.data.NotesRepository
import edu.josepinilla.notespmdm.databinding.ActivityMainBinding
import edu.josepinilla.notespmdm.ui.notes.DetailActivity
import kotlinx.coroutines.launch

/**
 * Class MainActivity
 * Actividad principal de la aplicación
 *
 * @author Jose Pinilla
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        val db = (application as NotesPMDMApplication).notesDatabase
        val dataSource = NotesDataSource(db.notesDao())
        val repository = NotesRepository(dataSource)
        MainViewModelFactory(repository)
    }

    private val adapter = NotesAdapter(
        onNoteClick = { idNote ->
            DetailActivity.navigate(this@MainActivity, idNote)
        },
        onNoteDeleteClick = { note, pos ->
            vm.deleteNote(note)
            val snackbar = Snackbar.make(
                binding.root,
                getString(R.string.txt_noteDeleted, note.title),
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.txt_undo){
                note.idNote = pos.toLong()
                vm.saveNote(note)
            }
            val params = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
            params.gravity = Gravity.BOTTOM
            params.setMargins(0, 0, 0, -binding.root.paddingBottom)
            snackbar.view.layoutParams = params
            snackbar.show()
        }
    )

    /**
     * Función onCreate
     * Cuando se crea la actividad
     *
     * @param savedInstanceState Bundle? estado de la instancia
     *
     * @return Unit
     *
     * @author Jose Pinilla
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter

        //Collect the list of notes to update the adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.currentNotes.collect {
                    if((it.isEmpty())) {
                        binding.tvNoNotes.visibility = android.view.View.VISIBLE
                    } else {
                        binding.tvNoNotes.visibility = android.view.View.GONE

                    }
                    adapter.submitList(it)
                }
            }
        }
    }

    /**
     * Función onStart
     * Cuando se inicia la actividad
     *
     * @return Unit
     *
     * @author Jose Pinilla
     */
    override fun onStart () {
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opt_add_note -> {
                    DetailActivity.navigate(this@MainActivity)
                    true
                }
                R.id.opt_ordenar_por_titulo-> {
                    lifecycleScope.launch {
                        vm.currentSortedNotes.collect {
                            adapter.submitList(it)
                        }
                    }
                    true
                }
                R.id.opt_desactivar_filtro -> {
                    lifecycleScope.launch {
                        vm.currentNotes.collect {
                            adapter.submitList(it)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
}

package edu.josepinilla.notespmdm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.josepinilla.notespmdm.databinding.ItemNoteBinding
import edu.josepinilla.notespmdm.model.Note

/**
 * Class NotesAdapter.kt
 * Adapter para el recyclerview de notas
 *
 * @author Jose Pinilla
 */
class NotesAdapter(
    val onNoteClick: (idNote: Long) -> Unit,
    val onNoteDeleteClick: (Note, pos: Int) -> Unit
) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(
    NotesDiffCallback()
) {
    /**
     * Class NotesViewHolder
     * Clase interna para el ViewHolder de las notas
     *
     * @param view Vista de la nota
     *
     * @author Jose Pinilla
     */
    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNoteBinding.bind(view)
        fun bind(note: Note) {
            binding.tvTituloNota.text = note.title
            binding.tvFecha.text = note.date
            binding.ivDelete.setOnClickListener {
                onNoteDeleteClick(note, note.idNote.toInt())
            }

            itemView.setOnClickListener {
                onNoteClick(note.idNote)
            }
        }
    }

    /**
     *
     * funcion onCreateViewHolder
     * cuando se crea un nuevo ViewHolder
     *
     * @param parent ViewGroup  Grupo de vistas
     * @param viewType Int indice del tipo de vista
     *
     * @return NotesViewHolder
     *
     * @author Jose Pinilla
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    /**
     *
     * funcion onBindViewHolder
     * cuando se enlaza un ViewHolder
     *
     * @param holder NotesViewHolder ViewHolder de la nota
     * @param position Int posicion de la nota
     *
     * @return Unit unidad de retorno
     *
     * @author Jose Pinilla
     *
     */
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 *
 * Clase NotesDiffCallback
 * Clase para comparar las notas
 *
 * @author Jose Pinilla
 *
 */
class NotesDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(
        oldItem: Note,
        newItem: Note
    ): Boolean {
        return oldItem.idNote == newItem.idNote
    }

    override fun areContentsTheSame(
        oldItem: Note,
        newItem: Note
    ): Boolean {
        return oldItem == newItem
    }
}
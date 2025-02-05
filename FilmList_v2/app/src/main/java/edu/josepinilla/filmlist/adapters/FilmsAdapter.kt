package edu.josepinilla.filmlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import edu.josepinilla.filmlist.R
import edu.josepinilla.filmlist.databinding.ItemFilmBinding
import edu.josepinilla.filmlist.model.Film

/**
 * Clase Adaptador para las peliculas (Film)
 */
class FilmsAdapter(
    val itemsList: MutableList<Film>,
    private val onItemsLongClick: (Film, pos: Int) -> Unit,
    val itemClick: (Film) -> Unit,
    ) :
    RecyclerView.Adapter<FilmsAdapter.ItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemsViewHolder {
        return ItemsViewHolder(
            ItemFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun getItemCount() = itemsList.size
    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    inner class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFilmBinding.bind(view)
        fun bind(item: Film) {
            itemView.setOnClickListener { itemClick(item) }
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.director

            //Se carga la imagen
            Glide.with(itemView)
                .load(item.cover)
                .centerCrop()
                .transform(RoundedCorners(30))
                .into(binding.ivImage)

            binding.tvYear.text = item.year.toString()
            binding.tvDuration.text = itemView.context.getString(R.string.txt_duration, item.duration.toString())
            binding.tvGenere.text = item.genre

            //Se define la pulsación larga para poder eliminar el item cuando se realice esta accion
            itemView.setOnLongClickListener {
                onItemsLongClick(item, adapterPosition)
                true
            }
        }
    }
}
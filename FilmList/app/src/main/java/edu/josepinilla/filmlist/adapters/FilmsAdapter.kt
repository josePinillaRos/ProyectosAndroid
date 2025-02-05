package edu.josepinilla.filmlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.josepinilla.filmlist.databinding.ItemFilmBinding
import edu.josepinilla.filmlist.model.Film

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
            Glide.with(itemView)
                .load(item.cover)
                .centerCrop()
                .into(binding.ivImage)
            binding.tvYear.text = item.year.toString()
            binding.tvDuration.text = item.duration.toString()
            binding.tvGenere.text = item.genre
            Glide.with(itemView)
                .load(item.cover)
                .centerCrop()
                .into(binding.ivImage)
            itemView.setOnLongClickListener {
                onItemsLongClick(item, adapterPosition)
                true
            }
        }
    }
}
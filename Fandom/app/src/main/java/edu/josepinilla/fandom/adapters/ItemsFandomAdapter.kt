package edu.josepinilla.fandom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.josepinilla.fandom.R
import edu.josepinilla.fandom.databinding.ItemFandomBinding
import edu.josepinilla.fandom.model.ItemFandom

class ItemsFandomAdapter (
    val itemsFandomList: MutableList<ItemFandom>,
    val itemClick: (ItemFandom) -> Unit,
    val imageClick: (ItemFandom) -> Unit
) :
RecyclerView.Adapter<ItemsFandomAdapter.ItemsFandomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemsFandomViewHolder {
        return ItemsFandomViewHolder(
            ItemFandomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun getItemCount() = itemsFandomList.size

    override fun onBindViewHolder(holder: ItemsFandomViewHolder, position: Int) {
        holder.bind(itemsFandomList[position])
    }

    inner class ItemsFandomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFandomBinding.bind(view)
        fun bind(item: ItemFandom) {
            binding.tvNombre.text = item.id.toString()
            binding.tvUniverso.text = item.title
            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .into(binding.ivPersonaje)

            Glide.with(itemView)
                .load(R.drawable.favorite)
                .centerCrop()
                .into(binding.ivFavorite)

            binding.ivFavorite.setOnClickListener { imageClick(item) }
            itemView.setOnClickListener { itemClick(item) }
        }
    }
}
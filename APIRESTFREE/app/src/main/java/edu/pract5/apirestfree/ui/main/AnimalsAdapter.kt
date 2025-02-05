package edu.pract5.apirestfree.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.pract5.apirestfree.R
import edu.pract5.apirestfree.databinding.ItemAnimalBinding
import edu.pract5.apirestfree.model.Animals

class AnimalsAdapter (
    val onClickAnimalsItem: (Animals) -> Unit,
    val onClickFavorite: (Animals) -> Unit
) : ListAdapter<Animals, AnimalsAdapter.AnimalsViewHolder>(AnimalsDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
            return AnimalsViewHolder(view)
        }

        override fun onBindViewHolder(holder: AnimalsViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item)
        }

        inner class AnimalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val binding = ItemAnimalBinding.bind(itemView)

            fun bind(item: Animals) {
                binding.tvAnimalName.text = item.name

                binding.root.setOnClickListener {
                    onClickAnimalsItem(item)
                }

                binding.ivFav.setOnClickListener {
                    onClickFavorite(item)
                    notifyItemChanged(adapterPosition)
                }

                binding.ivFav.setImageState(
                    intArrayOf(R.attr.state_on),
                    item.favorita == true
                )
            }
        }
}

class AnimalsDiffCallback : DiffUtil.ItemCallback<Animals>() {
    override fun areItemsTheSame(oldItem: Animals, newItem: Animals): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Animals, newItem: Animals): Boolean {
        return oldItem == newItem
    }
}
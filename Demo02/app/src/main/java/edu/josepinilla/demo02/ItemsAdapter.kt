package edu.josepinilla.demo02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.josepinilla.demo02.databinding.ItemsBinding
import edu.josepinilla.demo02.model.Items


class ItemsAdapter(
    val itemList: MutableList<Items>,
    val itemClick: (Items) -> Unit,
    val imageClick: (Items) -> Unit
): RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsAdapter.ItemsViewHolder {
        return ItemsViewHolder(
            ItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ItemsAdapter.ItemsViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() =  itemList.size


    inner class ItemsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemsBinding.bind(view)

        fun bind(item: Items) {
            binding.tvId.text = item.id.toString()
            binding.tvItem.text = item.title

            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .into(binding.ivItem)

            itemView.setOnClickListener { itemClick(item) }
            binding.ivItem.setOnClickListener { imageClick(item) }
        }
    }
}
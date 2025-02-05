package edu.josepinilla.demo03_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import edu.josepinilla.demo03_v2.databinding.ItemsBinding
import edu.josepinilla.demo03_v2.databinding.ListFragmentBinding

class ItemsAdapter : ListAdapter<Items,
        ItemsAdapter.ViewHolder>(ItemsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemsBinding.bind(view)
        fun bind(item: Items) {
            binding.tvId.text = item.id.toString()
            binding.tvTitle.text = item.title
            Glide.with(itemView)
                .load(item.image)
                .fitCenter()
                .transform(RoundedCorners(16))
                .into(binding.ivItem)
        }
    }

}
class ItemsDiffCallback : DiffUtil.ItemCallback<Items>() {
    override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem == newItem
    }
}
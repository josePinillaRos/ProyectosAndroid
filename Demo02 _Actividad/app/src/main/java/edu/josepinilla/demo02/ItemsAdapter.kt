package edu.josepinilla.demo02

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.demo02.databinding.ItemsBinding
import edu.josepinilla.demo02.model.Items


class ItemsAdapter(
    val itemList: MutableList<Items>,
    val itemClick: (Items) -> Unit,
    val imageClick: (Items) -> Unit,
    private val onItemLogClick: (Items, pos: Int) -> Unit
): RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }



    override fun onBindViewHolder(holder: ItemsAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() =  itemList.size


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
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
            itemView.setOnLongClickListener {
                Snackbar.make(
                    binding.root,
                    "¿Confirmas el borrado?",
                    Snackbar.LENGTH_SHORT
                ).setAction("No") {
                    itemList.add(adapterPosition, item)
                }.show()
                true
            }
        }
    }

    open class ItemLongClickListener {

    }
}
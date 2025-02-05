package edu.josepinilla.demo05.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import edu.josepinilla.demo05.databinding.CastLayoutBinding
import edu.josepinilla.demo05.model.cast.CastItem

class CastAdapter : ListAdapter <CastItem, CastAdapter.CastViewHolder>(CastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            CastLayoutBinding.inflate(
                LayoutInflater.from(parent.context), // no tenemos layout inflater
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bind = CastLayoutBinding.bind(view)

        fun bind(cast: CastItem) {
            bind.tvNameCast.setText(cast.person!!.name)

            Glide.with(itemView.context)
                .load(cast.person.image!!.medium)
                .transform(
                    FitCenter(),
                    RoundedCorners(8)
                )
                .into(bind.ivCast)
        }
    }
}

class CastDiffCallback : DiffUtil.ItemCallback<CastItem>() {
    override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return (oldItem.person!!.id == newItem.person!!.id) &&
                (oldItem.character!!.id == newItem.character!!.id)
    }

    override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return oldItem == newItem
    }
}
package edu.josepinilla.demo05.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import edu.josepinilla.demo05.R
import edu.josepinilla.demo05.databinding.ItemShowBinding
import edu.josepinilla.demo05.model.shows.ShowsItem

/**
 * class ShowsAdapter
 * adaptador para el RecyclerView de las series
 *
 * @param onClickShowItem
 * @param onClickShowFavorite
 * @param onClickShowWatch
 *
 * @author Jose Pinilla
 */
class ShowsAdapter(
    val onClickShowItem : (idShow : Int) -> Unit,
    val onClickShowFavorite: (show: ShowsItem) -> Unit,
    val onClickShowWatch: (show: ShowsItem) -> Unit

) : ListAdapter<ShowsItem, ShowsAdapter.ShowsViewHolder>(ShowsDiffCallback()){

    /**
     * inner class ShowsViewHolder
     * clase interna que contiene la vista
     *
     * @param view vista de la serie
     *
     * @return bind de la vista
     *
     * @author Jose Pinilla
     */
    inner class ShowsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val bind = ItemShowBinding.bind(view)
        fun bind(show: ShowsItem){
            bind.tvName.setText(show.name!!.uppercase())
            if(show.network != null) {
                bind.tvProducer.setText(show.network.name)
            }
            bind.tvAverage.setText(show.rating!!.average.toString())
            bind.tvGender.setText(show.genres!!.joinToString(","))

            Glide.with(itemView.context)
                .load(show.image!!.medium)
                .transform(FitCenter(), RoundedCorners(8))
                .into(bind.ivIcon)

            itemView.setOnClickListener {
                onClickShowItem(show.id!!)
            }

            bind.ivFav.setOnClickListener {
                onClickShowFavorite(show)
                notifyItemChanged(adapterPosition)
            }
            bind.ivWatched.setOnClickListener {
                onClickShowWatch (show)
                notifyItemChanged(adapterPosition)
            }

            bind.ivFav.setImageState(
                intArrayOf(R.attr.state_on),
                show.favorite
            )

            bind.ivWatched.setImageState(
                intArrayOf(R.attr.state_on),
                show.watched
            )
        }
    }

    /**
     * fun onCreateViewHolder
     * crea una nueva vista
     *
     * @param parent vista padre
     * @param viewType tipo de vista
     *
     * @return ShowsViewHolder que contiene la vista
     *
     * @author Jose Pinilla
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(
            ItemShowBinding.inflate
                (LayoutInflater.from
                (parent.context),
                parent,
                false).root)
    }

    /**
     * fun onBindViewHolder
     * Hace el bind de los datos con la vista
     *
     * @param holder vista de la serie
     * @param position posición de la serie
     *
     * @author Jose Pinilla
     */
    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * class ShowsDiffCallback
 * clase que compara dos elementos
 *
 * @author Jose Pinilla
 */
class ShowsDiffCallback : DiffUtil.ItemCallback<ShowsItem>() {
    /**
     * fun areItemsTheSame
     * compara si los elementos son iguales
     *
     * @param oldItem elemento antiguo
     * @param newItem elemento nuevo
     *
     * @return true si son iguales, false si no
     *
     * @author Jose Pinilla
     */
    override fun areItemsTheSame(oldItem: ShowsItem, newItem: ShowsItem): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * fun areContentsTheSame
     * compara si el contenido es el mismo
     *
     * @param oldItem elemento antiguo
     * @param newItem elemento nuevo
     *
     * @return true si son iguales, false si no
     *
     * @author Jose Pinilla
     */
    override fun areContentsTheSame(oldItem: ShowsItem, newItem: ShowsItem): Boolean {
        return oldItem == newItem
    }
}
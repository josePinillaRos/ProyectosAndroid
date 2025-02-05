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

/**
 * class CastAdapter
 * adaptador para el RecyclerView de los actores de la serie
 *
 *  @author Jose Pinilla
 */
class CastAdapter : ListAdapter <CastItem, CastAdapter.CastViewHolder>(CastDiffCallback()) {

    /**
     * fun onCreateViewHolder
     * crea una nueva vista
     *
     * @param parent
     * @param viewType
     * @return CastViewHolder que contiene la vista
     *
     * @author Jose Pinilla
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            CastLayoutBinding.inflate(
                LayoutInflater.from(parent.context), // no tenemos layout inflater
                parent,
                false
            ).root
        )
    }

    /**
     * fun onBindViewHolder
     * Hace el bind de los datos con la vista
     *
     * @param holder
     * @param position
     *
     * @author Jose Pinilla
     */
    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * inner class CastViewHolder
     * clase interna que contiene la vista
     *
     * @param view
     *
     * @author Jose Pinilla
     */
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

/**
 * class CastDiffCallback
 * clase que compara los elementos de la lista
 *
 * @author Jose Pinilla
 */
class CastDiffCallback : DiffUtil.ItemCallback<CastItem>() {
    /**
     * fun areItemsTheSame
     * compara si los elementos son los mismos
     *
     * @param oldItem el item anterior
     * @param newItem el item nuevo
     * @return Boolean si son iguales o no
     *
     * @author Jose Pinilla
     */
    override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return (oldItem.person!!.id == newItem.person!!.id) &&
                (oldItem.character!!.id == newItem.character!!.id)
    }

    /**
     * fun areContentsTheSame
     * compara si el contenido de los elementos  del cast son iguales
     *
     *
     * @param oldItem el item anterior
     * @param newItem el item nuevo
     *
     * @return Boolean si son iguales o no
     *
     * @author Jose Pinilla
     */
    override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return oldItem == newItem
    }
}
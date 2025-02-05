package edu.josepinilla.demo04_v2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.josepinilla.demo04_v2.R
import edu.josepinilla.demo04_v2.databinding.ItemSupersBinding
import edu.josepinilla.demo04_v2.model.SupersWithEditorial

class SupersAdapter(
    private val onCLickSuperHero: (SupersWithEditorial) -> Unit,
    private val favListener: (SupersWithEditorial) -> Unit,
    private val onDeleteSuper: (SupersWithEditorial) -> Unit
) : ListAdapter<SupersWithEditorial,SupersAdapter.SupersWithEditorialVH>(
    SupersDiffCallback()
) {
    inner class SupersWithEditorialVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSupersBinding.bind(view)
        fun bind(superWithEditorial: SupersWithEditorial) {
            binding.tvSuperName.text = superWithEditorial.superHero.superName
            binding.tvEditorial.text = superWithEditorial.editorial.name

            binding.ivFav.setImageState(
                intArrayOf(R.attr.state_fav),
                superWithEditorial.superHero.favorite == 1
            )

            itemView.setOnClickListener {
                onCLickSuperHero(superWithEditorial)
            }

            binding.ivFav.setOnClickListener {
                favListener(superWithEditorial)
            }

            //Establece la pulsación larga del item y llama a eliminar super heroe
            itemView.setOnLongClickListener {
                onDeleteSuper(superWithEditorial)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupersWithEditorialVH {
        return SupersWithEditorialVH(
            ItemSupersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: SupersWithEditorialVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class SupersDiffCallback : DiffUtil.ItemCallback<SupersWithEditorial>() {
    override fun areItemsTheSame(
        oldItem: SupersWithEditorial,
        newItem: SupersWithEditorial
    ): Boolean {
        //aqui comparamos los ids de los superhéroes
        return (oldItem.superHero.idSuper == newItem.superHero.idSuper) &&
                (oldItem.editorial.idEd == newItem.editorial.idEd)
    }

    override fun areContentsTheSame(
        oldItem: SupersWithEditorial,
        newItem: SupersWithEditorial
    ): Boolean {
        //Aqui los comparamos enteros por objeto
        return oldItem == newItem
        }
}
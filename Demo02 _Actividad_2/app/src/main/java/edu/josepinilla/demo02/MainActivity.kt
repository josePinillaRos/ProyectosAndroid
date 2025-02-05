package edu.josepinilla.demo02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.demo02.databinding.ActivityMainBinding
import edu.josepinilla.demo02.model.Items

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val elemento = Items.items
        //Log.d("Elemento", "onCreate: ${elemento.size}")
        itemsAdapter = ItemsAdapter(
            elemento,
            onItemsLongClick = { item, pos ->
                elemento.removeAt(pos)
                itemsAdapter.notifyItemRemoved(pos)
                Snackbar.make(
                    binding.root,
                    "¿Rehacer eliminación?",
                    Snackbar.LENGTH_SHORT
                ).setAction("Rehacer") {
                    elemento.add(pos, item)
                    itemsAdapter.notifyItemInserted(pos)
                }.show()


            },

            itemClick = { item ->
                DetailActivity.navigateToDetail(this@MainActivity, item)
            },
            imageClick = { item ->
                val snackbar =
                    Snackbar.make(
                        binding.root,
                        "Image clicked: ${item.title}",
                        Snackbar.LENGTH_SHORT
                    )


                //Ajuste posicion snackbar
                val params = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
                params.gravity = Gravity.BOTTOM
                params.setMargins(0, 0, 0, -binding.root.paddingBottom)
                snackbar.view.layoutParams = params
                snackbar.show()
            }
        )

        binding.recyclerView.adapter = itemsAdapter
    }
}
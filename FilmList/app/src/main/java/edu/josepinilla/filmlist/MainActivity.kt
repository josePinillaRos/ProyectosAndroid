package edu.josepinilla.filmlist

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.filmlist.adapters.FilmsAdapter
import edu.josepinilla.filmlist.databinding.ActivityMainBinding
import edu.josepinilla.filmlist.model.Film.Companion.films
import edu.josepinilla.filmlist.utils.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemsAdapter: FilmsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        films = Utils.readRawFile(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemsAdapter = FilmsAdapter(
            films,
            onItemsLongClick = { item, pos ->
                films.removeAt(pos)
                itemsAdapter.notifyItemRemoved(pos)
                val snackbar =
                Snackbar.make(
                    binding.root,
                    item.title + " deleted",
                    Snackbar.LENGTH_SHORT
                ).setAction("UNDO") {
                    films.add(pos, item)
                    itemsAdapter.notifyItemInserted(pos)
                }

                val params = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
                params.gravity = Gravity.BOTTOM
                params.setMargins(0, 0, 0, -binding.root.paddingBottom)
                snackbar.view.layoutParams = params
                snackbar.show()
            },
            itemClick = { item ->
                Toast.makeText(
                    this,
                    "Item clicked: ${item.title}",
                    Toast.LENGTH_SHORT
                ).show()


            }
        )
        binding.recyclerView.adapter = itemsAdapter
    }
}
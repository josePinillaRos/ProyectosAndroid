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
import androidx.core.os.BundleCompat
import edu.josepinilla.filmlist.model.Film

/**
 * Clase Main, inicia la aplicacion
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemsAdapter: FilmsAdapter

    //Método que inicia la app, infla el mainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Aquí se comprueba el estado de la UI parcelada, para mantener la persistencia de datos
        //al girar el movil.
        if (savedInstanceState != null) {
            films = BundleCompat.getParcelableArrayList(savedInstanceState, "FILMS_LIST", Film::class.java) ?: Utils.readRawFile(this)
        } else {
            films = Utils.readRawFile(this)
        }

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

                if (films.isEmpty()) {
                    // Si no hay películas, mostramos la vista de "No films"
                    setContentView(R.layout.nofilms)
                } else {


                    val snackbar =
                        Snackbar.make(
                            binding.root,
                            getString(R.string.txt_film_deleted, item.title),
                            Snackbar.LENGTH_SHORT
                        ).setAction(R.string.txt_undo) {
                            films.add(pos, item)
                            itemsAdapter.notifyItemInserted(pos)
                        }

                    val params = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
                    params.gravity = Gravity.BOTTOM
                    params.setMargins(0, 0, 0, -binding.root.paddingBottom)
                    snackbar.view.layoutParams = params
                    snackbar.show()
                }
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


        if (films.isNotEmpty()) {
            binding.recyclerView.adapter = itemsAdapter
        } else {
            // Si no hay películas al iniciar, mostramos la vista "No films"
            setContentView(R.layout.nofilms)
        }
    }

    //Se sobreescribe el onSaveInstanceState para mantener la persistencia de datos
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("FILMS_LIST", ArrayList(films))
    }
}
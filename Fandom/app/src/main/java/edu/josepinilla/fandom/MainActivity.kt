package edu.josepinilla.fandom

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.fandom.adapters.ItemsFandomAdapter
import edu.josepinilla.fandom.databinding.ActivityMainBinding
import edu.josepinilla.fandom.model.ItemFandom

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isFavorite = false
    private lateinit var itemsFandomAdapter: ItemsFandomAdapter

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

       /* val imageView: ImageView = findViewById(R.id.ivFavorite)

        // Establece un click listener para cambiar el estado de favorito
        imageView.setOnClickListener {
            isFavorite = !isFavorite // Cambia el estado
            imageView.isSelected = isFavorite // Usa state_selected para el selector
        }*/

        itemsFandomAdapter = ItemsFandomAdapter(
            ItemFandom.items,
            itemClick = { item ->
                Toast.makeText(
                    this,
                    "Item clicked: ${item.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }, imageClick = { item ->
                Snackbar.make(
                    binding.root,
                    "Image clicked: ${item.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerView.adapter = itemsFandomAdapter
    }
}
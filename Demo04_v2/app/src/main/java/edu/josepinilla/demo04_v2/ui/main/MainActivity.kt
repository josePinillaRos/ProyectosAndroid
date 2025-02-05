package edu.josepinilla.demo04_v2.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.demo04_v2.MyRoomApplication
import edu.josepinilla.demo04_v2.R
import edu.josepinilla.demo04_v2.adapters.SupersAdapter
import edu.josepinilla.demo04_v2.data.SupersDataSource
import edu.josepinilla.demo04_v2.data.SupersRepository
import edu.josepinilla.demo04_v2.databinding.ActivityMainBinding
import edu.josepinilla.demo04_v2.databinding.LayoutEditorialBinding
import edu.josepinilla.demo04_v2.ui.supers.SupersActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        val db = (application as MyRoomApplication).supersDatabase
        val dataSource = SupersDataSource(db.supersDao())
        val repository = SupersRepository(dataSource)
        MainViewModelFactory(repository)
    }

    private val adapter = SupersAdapter(
        onCLickSuperHero = {supersWithEditorial ->
            SupersActivity.navigate(this@MainActivity, supersWithEditorial.superHero.idSuper)
        },
        favListener = { supersWithEditorial ->
            vm.updateFavorite(supersWithEditorial)
        },

        //Llama al metodo del Supers Adapter y establece un snackbar de confirmación para el borrado
        onDeleteSuper = {supersWithEditorial ->
             Snackbar.make(
                binding.root,
                R.string.txt_delete,
                Snackbar.LENGTH_SHORT
            ).setAction(
                R.string.txt_deshacer) {
                vm.deleteSuper(supersWithEditorial)
            }.show()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter

        //Collect the list of superheroes to update the adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.currentSuperHero.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
    override fun onStart(){
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opt_add_editorial -> {
                    addEditorial()
                    true
                }

                R.id.opt_add_superhero -> {
                    lifecycleScope.launch {
                        vm.numEditorials.collect {
                            Log.i("MainActivity", "Num of Editorials: $it")
                            if( it > 0)
                                SupersActivity.navigate(this@MainActivity)
                            else Toast.makeText(
                                    this@MainActivity,
                                    "No hay editoriales creadas",
                                    Toast.LENGTH_SHORT
                                ).show()
                                this.cancel()
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun addEditorial() {
        val bindDialog = LayoutEditorialBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(this).apply {
            setTitle(R.string.txt_opt_add_editorial)
            setView(bindDialog.root)
            setPositiveButton(android.R.string.ok, null)
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = bindDialog.tietEditorialName.text.toString()
                if (name.isEmpty()) {
                    bindDialog.tilEditorialName.error =
                        getString(R.string.txt_empty_field)
                } else {
                    Log.i("Dialog Editorial", "Name: $name")
                    vm.saveEditorial(name)

                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}
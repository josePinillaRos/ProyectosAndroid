package edu.josepinilla.demo04_v2.ui.supers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.josepinilla.demo04_v2.MyRoomApplication
import edu.josepinilla.demo04_v2.R
import edu.josepinilla.demo04_v2.data.SupersDataSource
import edu.josepinilla.demo04_v2.data.SupersRepository
import edu.josepinilla.demo04_v2.databinding.ActivitySupersBinding
import edu.josepinilla.demo04_v2.model.SuperHero
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SupersActivity : AppCompatActivity() {
    private val TAG = SupersActivity::class.java.simpleName
    private lateinit var binding: ActivitySupersBinding
    private var editorialIdAux = 0
    private val vm: SupersViewModel by viewModels {
        val db = (application as MyRoomApplication).supersDatabase
        val dataSource = SupersDataSource(db.supersDao())
        val repository = SupersRepository(dataSource)
        val superIdAux = intent.getIntExtra(SUPER_ID, 0)
        SupersViewModelFactory(repository, superIdAux)
    }

    companion object {
        const val SUPER_ID = "super_id"
        fun navigate(activity: Activity, superId: Int = 0) {
            val intent = Intent(activity, SupersActivity::class.java).apply {
                putExtra(SUPER_ID, superId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySupersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSave.setOnClickListener{
            val superHero = SuperHero(
                superName = binding.tiedSuperName.text.toString().trim(),
                realName = binding.tiedRealName.text.toString().trim(),
                favorite = if (binding.cbFavorite.isChecked) 1 else 0,
                idEditorial = editorialIdAux
            )
            vm.saveSuper(superHero)
            finish()
        }

        binding.btnEditorial.setOnClickListener {
            showEditorials()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(vm.stateSupers, vm.stateEditorial) { superHero, editorial ->
                    Log.d(TAG, "superHero: $superHero")
                    Log.d(TAG, "editorial: $editorial")

                    binding.tiedSuperName.setText(superHero.superName)
                    binding.tiedRealName.setText(superHero.realName)
                    binding.cbFavorite.isChecked = superHero.favorite == 1

                    editorialIdAux = editorial.idEd
                    binding.tvEditorial.text = editorial.name

                }.collect()
            }
        }
        //Aqui ya no se ejecuta codigo
    }

    private fun showEditorials() {
        lifecycleScope.launch {
            vm.allEditorial.collect {
                MaterialAlertDialogBuilder(this@SupersActivity).apply {
                    setTitle(getString(R.string.txt_editorial))
                    setItems(it.map {it.name}.toTypedArray()){ dialog, which ->
                        editorialIdAux = it[which].idEd
                        binding.tvEditorial.text = it[which].name
                        dialog.dismiss()
                    }
                }.show()
            }
        }
    }
}
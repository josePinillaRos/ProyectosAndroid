package edu.josepinilla.demo01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import edu.josepinilla.demo01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            if (!binding.tvResult.text.isNullOrEmpty())
                putString("RESULTADO", binding.tvResult.text.toString())

            //Guardar estado botones validar y reiniciar
            putBoolean("ESTADO_BOTONES", binding.btnReinicio.isEnabled)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            if (containsKey("RESULTADO"))
                binding.tvResult.text = getString("RESULTADO")

            //restaurar valores de botones reiniciar y validar al girar la pantalla
            if (getBoolean("ESTADO_BOTONES")) {
                binding.button.isEnabled = false
                binding.btnReinicio.isEnabled = true
            }
        }
    }

    //Cambio de Toast por SnackBar
    private fun modulosNoSeleccionados() {
        Snackbar.make(binding.root, "Debes seleccionar almenos un módulo", Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()

        validarFormulario()
        //Funcion del on click listener de reiniciar formulario
        reiniciarFormulario()
    }

    private fun validarFormulario() {
        binding.button.setOnClickListener {
            Log.d(TAG, "onStart: pulsado el botón")


            binding.tvResult.text = null
            val tName = binding.tietName.text.toString().trim()

            if(tName.isBlank()) {
                binding.tilName.error = getString(R.string.txt_warning)
            } else {
                binding.tilName.error = null
            }

            //Comprobar campo vacío apellido
            binding.tvResult.text = null
            val tSurname = binding.tietSurName.text.toString().trim()

            if(tSurname.isBlank()) {
                binding.tilSurname.error = getString(R.string.txt_warning)
            } else {
                binding.tilSurname.error = null
            }

            //Comprobar campo vacío cumpleaños
            binding.tvResult.text = null
            val tBirthDate = binding.tietBirthDate.text.toString().trim()

            if(tBirthDate.isBlank()) {
                binding.tilBirthday.error = getString(R.string.txt_warning)
            } else {
                binding.tilBirthday.error = null
            }

            val moduleCheck = with(binding) {
                cbDI.isChecked || cbPMDM.isChecked || cbADA.isChecked ||
                        cbPSP.isChecked || cbSGE.isChecked
            }

            if(moduleCheck) {
                binding.tvResult.text = getString(
                    R.string.txt_result1,
                    tName,
                    binding.tietSurName.text.toString().trim(),
                    binding.tietBirthDate.text.toString().trim()
                )
                val carnet = if (binding.rbNo.isChecked) getString(R.string.txt_no) else
                    getString(R.string.txt_yes)
                val turn = if (binding.swTurn.isChecked) getString(R.string.txt_turn) else
                    getString(R.string.txt_morning)
                binding.tvResult.append(
                    getString(
                        R.string.txt_result2,
                        carnet, turn
                    )
                )

                val modules = mutableListOf<String>()
                val view = binding.root.getChildAt(1)

                if (view is ViewGroup) {
                    val linear = view.getChildAt(0) as LinearLayout
                    for (j in 0 until linear.childCount) {
                        val viewChild = linear.getChildAt(j)
                        if (viewChild is androidx.appcompat.widget.AppCompatCheckBox) {
                            Log.d(
                                "MainActivity",
                                "CheckBox: ${viewChild.text} is ${viewChild.isChecked}"
                            )
                            if (viewChild.isChecked)
                                modules.add(viewChild.text.toString())
                        }
                    }
                }
                binding.tvResult.append(
                    getString(
                        R.string.txt_result3,
                        modules.joinToString("\n")
                    )
                )
                //Mostrar reinicio formulario y ocultar validar formulario
                binding.button.isEnabled = false
                binding.btnReinicio.isEnabled = true

            } else {
                modulosNoSeleccionados()
            }
        }
    }

    private fun reiniciarFormulario() {
        binding.btnReinicio.setOnClickListener {
            vaciarCampos()

            vaciarCheckBox()

            //Restaurar estado botones
            binding.tilBirthday.clearFocus()
            binding.button.isEnabled = true
            binding.btnReinicio.isEnabled = false
        }
    }

    private fun vaciarCheckBox() {
        val view2 = binding.root.getChildAt(1)

        if (view2 is ViewGroup) {
            val linear = view2.getChildAt(0) as LinearLayout
            for (j in 0 until linear.childCount) {
                val viewChild = linear.getChildAt(j)
                if (viewChild is AppCompatCheckBox) {
                    Log.d(
                        "MainActivity",
                        "CheckBox: ${viewChild.text} is ${viewChild.isChecked}"
                    )
                    viewChild.isChecked = false
                }
            }
        }
    }

    private fun vaciarCampos() {
        binding.tvResult.text = null
        binding.tietSurName.text = null
        binding.tietBirthDate.text = null
        binding.tietName.text = null

        binding.rbNo.isChecked = true

        binding.swTurn.isChecked = false
    }
}
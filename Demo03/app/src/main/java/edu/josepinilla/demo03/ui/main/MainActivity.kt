package edu.josepinilla.demo03.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import edu.josepinilla.demo03.ui.fragments.FragmentAdd
import edu.josepinilla.demo03.ui.fragments.FragmentList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Fragments to work
    private lateinit var listFragment: FragmentList
    private lateinit var addFragment: FragmentAdd

    //MainViewModel link
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        listFragment = FragmentList()
        addFragment = FragmentAdd()

        if (mainViewModel.fragmentShowed == null)
            loadFragment(listFragment)
        else {
            when (mainViewModel.fragmentShowed) {
                listFragment.javaClass.simpleName -> loadFragment(listFragment)
                addFragment.javaClass.simpleName -> loadFragment(addFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mBottonNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.op_list -> loadFragment(listFragment)
                R.id.op_add -> loadFragment(addFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mFrameLayout.id, fragment)
            .commit()

       mainViewModel.setFragmentShowed(fragment.javaClass.simpleName)
    }
}
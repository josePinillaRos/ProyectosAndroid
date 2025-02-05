package edu.josepinilla.demo03_v2

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.josepinilla.demo03_v2.databinding.AddFragmentBinding

class FragmentAdd : Fragment() {

    private lateinit var binding: AddFragmentBinding
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            binding.tilTitle.error = null
            binding.tilDesc.error = null
            val title = binding.tietTitle.text.toString().trim()
            val description = binding.tietDesc.text.toString().trim()
            if (title.isEmpty()) {
                binding.tilTitle.error = getString(R.string.error_title)
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.tilDesc.error = getString(R.string.error_desc)
                return@setOnClickListener
            }
            sharedViewModel.addItem(Items(title = title, description = description))
            binding.tietTitle.text?.clear()
            binding.tietDesc.text?.clear()

            Log.d("FragmentList", "onViewCreated: ${Items.items.size}")

            // Hide the keyboard
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
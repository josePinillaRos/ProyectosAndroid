package edu.josepinilla.demo03_v2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.josepinilla.demo03_v2.databinding.ArchivedFragmentBinding

class FragmentArchived : Fragment() {
    private lateinit var binding: ArchivedFragmentBinding
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArchivedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemsAdapter(
            onItemClick = { showItemDialog(it) },
            onArchiveClick = { /* Aquí no se usa la función de archivado en este fragmento */ }
        )

        Log.d("FragmentList", "onViewCreated: ${Items.items.size}")
        adapter.submitList(sharedViewModel.fetchItems())
        binding.mRecycler.layoutManager = LinearLayoutManager(context)
        binding.mRecycler.adapter = adapter
    }

    private fun showItemDialog(item: Items) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(item.title)
            .setMessage(item.description)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
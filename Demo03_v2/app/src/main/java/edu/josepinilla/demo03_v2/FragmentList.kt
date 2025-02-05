package edu.josepinilla.demo03_v2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.josepinilla.demo03_v2.databinding.ListFragmentBinding

class FragmentList : Fragment() {
    private lateinit var binding: ListFragmentBinding
    private val adapter = ItemsAdapter()

    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FragmentList", "onViewCreated: ${Items.items.size}")
        adapter.submitList(sharedViewModel.fetchItems())
        binding.mRecycler.layoutManager = LinearLayoutManager(context)
        binding.mRecycler.adapter = adapter
    }
}
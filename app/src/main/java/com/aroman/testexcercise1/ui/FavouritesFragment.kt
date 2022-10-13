package com.aroman.testexcercise1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aroman.testexcercise1.data.FakeMarkerListRepoImpl
import com.aroman.testexcercise1.databinding.FragmentFavouritesBinding
import com.aroman.testexcercise1.utils.attachLeftSwipeHelper

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val favouritesAdapter = FavouritesAdapter { position ->
        onItemClick(position)
    }

    private val viewModel = FavouritesViewModel(FakeMarkerListRepoImpl())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initViewModel()
        renderData()
    }

    private fun initRecycler() {
        binding.markersRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = favouritesAdapter
        }.attachLeftSwipeHelper { viewHolder ->
            viewModel.deleteMarker(favouritesAdapter.getData()[viewHolder.adapterPosition])
            renderData()
        }
    }

    private fun initViewModel() {
        viewModel.markerList.observe(viewLifecycleOwner) {
            favouritesAdapter.setData(it)
        }
    }

    private fun renderData() {
        viewModel.loadMarkerList()
    }

    private fun onItemClick(position: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
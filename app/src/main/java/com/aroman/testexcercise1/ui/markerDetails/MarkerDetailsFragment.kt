package com.aroman.testexcercise1.ui.markerDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.aroman.testexcercise1.databinding.FragmentMarkerDetailsBinding
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarkerDetailsFragment : Fragment() {
    private var _binding: FragmentMarkerDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarkerDetailsViewModel by viewModel()
    private lateinit var marker: MarkerEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarkerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val MARKER = "MARKER"

        fun newInstance(marker: MarkerEntity) = MarkerDetailsFragment().apply {
            arguments = bundleOf(MARKER to marker)
            this.marker = marker
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initSaveButton()
    }

    private fun initView() {
        binding.apply {
            name.setText(marker.name)
            latitude.setText(marker.lat.toString())
            longitude.setText(marker.long.toString())
            annotation.setText(marker.annotation)
        }
    }

    private fun initSaveButton() {
        binding.buttonSave.setOnClickListener {
            viewModel.updateMarker(collectMarker())
        }
    }

    private fun collectMarker() = MarkerEntity(
        id = marker.id,
        name = binding.name.text.toString(),
        lat = binding.latitude.text.toString().toDoubleOrNull() ?: -1.0,
        long = binding.longitude.text.toString().toDoubleOrNull() ?: -1.0,
        annotation = binding.annotation.text.toString()
    )

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
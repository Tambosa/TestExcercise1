package com.aroman.testexcercise1.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import com.aroman.testexcercise1.databinding.RecyclerItemMarkerBinding

class FavouritesViewHolder(
    private val binding: RecyclerItemMarkerBinding,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (position: Int) -> Unit
        ): FavouritesViewHolder =
            FavouritesViewHolder(
                RecyclerItemMarkerBinding.inflate(LayoutInflater.from(parent.context)),
                onItemClick
            )
    }

    fun bind(marker: MarkerEntity) {
        binding.apply {
            name.text = marker.name
            latitude.text = marker.lat.toString()
            longitude.text = marker.long.toString()
            annotation.text = marker.annotation
        }
    }
}
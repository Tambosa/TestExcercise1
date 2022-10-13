package com.aroman.testexcercise1.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import com.aroman.testexcercise1.utils.FavouritesViewHolder

class FavouritesAdapter(private val onItemClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<FavouritesViewHolder>() {
    private var data: List<MarkerEntity> = emptyList()

    fun getData() = data

    fun setData(materialsList: List<MarkerEntity>) {
        data = materialsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder =
        FavouritesViewHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
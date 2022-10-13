package com.aroman.testexcercise1.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aroman.testexcercise1.R
import com.aroman.testexcercise1.databinding.ActivityMapsBinding
import com.aroman.testexcercise1.domain.MarkerListRepo
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import com.aroman.testexcercise1.ui.favourites.FavouritesFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var allMarkers: List<MarkerEntity>

    private val viewModel: MapsViewModel by viewModel()
    private val repo: MarkerListRepo by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
        updateMarkerList()
    }

    private fun initViewModel() {
        viewModel.markerList.observe(this) {
            allMarkers = it
        }
    }

    private fun updateMarkerList() {
        viewModel.loadMarkerList()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.loadMarkerList()

        mMap.setOnMapClickListener {
            val marker = MarkerEntity(
                id = 0,
                name = "",
                lat = String.format("%.9f", it.latitude).toDouble(),
                long = String.format("%.9f", it.longitude).toDouble(),
                annotation = ""
            )
            viewModel.insertMarker(marker)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(marker.lat, marker.long))
                    .title(marker.name)
                    .snippet(marker.annotation)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favourites -> {
                startFavouritesFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startFavouritesFragment() {
        val pointerFragment = supportFragmentManager.findFragmentByTag(FAVOURITES_FRAGMENT_TAG)
        if (pointerFragment == null || !pointerFragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.map, FavouritesFragment(), FAVOURITES_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        const val FAVOURITES_FRAGMENT_TAG = "favourites"
    }
}
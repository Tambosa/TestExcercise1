package com.aroman.testexcercise1.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aroman.testexcercise1.R
import com.aroman.testexcercise1.databinding.ActivityMapsBinding
import com.aroman.testexcercise1.ui.favourites.FavouritesFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var allMarkers: ArrayList<MarkerOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        allMarkers = arrayListOf()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        allMarkers.add(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.addMarker(allMarkers[0])
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMapClickListener {
            allMarkers.add(MarkerOptions().position(it).title("new Marker ${allMarkers.size}"))
            mMap.addMarker(allMarkers[allMarkers.size - 1])
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
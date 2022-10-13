package com.aroman.testexcercise1.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.aroman.testexcercise1.R
import com.aroman.testexcercise1.databinding.ActivityMapsBinding
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import com.aroman.testexcercise1.ui.favourites.FavouritesFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var allMarkers = listOf<MarkerEntity>()
    private var visibleMarkers = arrayListOf<Marker>()

    private val viewModel: MapsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
        initOnBackPressed()
    }

    private fun initViewModel() {
        viewModel.markerList.observe(this) {
            if (it.isEmpty()) {
                addDemonstrationMarker()
            }
            allMarkers = it
            attachMarkers()
        }
    }

    private fun initOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFragmentManager.popBackStack()
                detachOldMarkers()
                updateMarkerList()
            }
        })
    }

    private fun detachOldMarkers() {
        for (visibleMarker in visibleMarkers) {
            visibleMarker.remove()
        }
    }

    private fun updateMarkerList() {
        viewModel.loadMarkerList()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initCustomInfoWindow()
        updateMarkerList()
        initOnMapClickListener()
        initOnMarkerClickListener()
    }

    private fun initOnMarkerClickListener() {
        mMap.setOnMarkerClickListener { marker ->
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 2F));
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
    }

    private fun addDemonstrationMarker() {
        viewModel.insertMarker(
            MarkerEntity(
                id = 0,
                name = getString(R.string.moscow),
                lat = 55.7558,
                long = 37.6173,
                annotation = getString(R.string.moscow_annotation)
            )
        )
    }

    private fun initCustomInfoWindow() {
        mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val info = LinearLayout(this@MapsActivity)
                info.orientation = LinearLayout.VERTICAL
                val title = TextView(this@MapsActivity)
                title.setTextColor(Color.BLACK)
                title.gravity = Gravity.CENTER
                title.setTypeface(null, Typeface.BOLD)
                title.text = marker.title
                val snippet = TextView(this@MapsActivity)
                snippet.setTextColor(Color.GRAY)
                snippet.text = marker.snippet
                info.addView(title)
                info.addView(snippet)
                return info
            }
        })
    }

    private fun attachMarkers() {
        for (marker in allMarkers) {
            attachMarker(marker)
        }
    }

    private fun initOnMapClickListener() {
        mMap.setOnMapClickListener {
            val marker = MarkerEntity(
                id = 0,
                name = getString(R.string.new_marker),
                lat = String.format("%.9f", it.latitude).toDouble(),
                long = String.format("%.9f", it.longitude).toDouble(),
                annotation = ""
            )
            viewModel.insertMarker(marker)
            attachMarker(marker)
        }
    }

    private fun attachMarker(marker: MarkerEntity) {
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(marker.lat, marker.long))
                .title(marker.name)
                .snippet(marker.annotation)
        )?.let {
            if (marker.name.isNotEmpty() || marker.annotation.isNotEmpty()) {
                it.showInfoWindow()
            }
            visibleMarkers.add(it)
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
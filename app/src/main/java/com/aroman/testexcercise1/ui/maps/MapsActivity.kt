package com.aroman.testexcercise1.ui.maps

import android.Manifest
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aroman.testexcercise1.R
import com.aroman.testexcercise1.databinding.ActivityMapsBinding
import com.aroman.testexcercise1.domain.entities.MarkerEntity
import com.aroman.testexcercise1.ui.favourites.FavouritesFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showUserLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_alert),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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
                binding.findMeButton.visibility = View.VISIBLE
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
        initFindMeButton()
    }

    private fun initFindMeButton() {
        binding.findMeButton.setOnClickListener {
            locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun initOnMarkerClickListener() {
        mMap.setOnMarkerClickListener { marker ->
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

    private fun showUserLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val marker = MarkerEntity(
                        id = 0,
                        name = getString(R.string.me),
                        lat = String.format("%.9f", location.latitude).toDouble(),
                        long = String.format("%.9f", location.longitude).toDouble(),
                        annotation = ""
                    )
                    viewModel.insertMarker(marker)
                    attachMarker(marker)

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude),
                            14F
                        )
                    )
                }
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
        binding.findMeButton.visibility = View.GONE
        val pointerFragment = supportFragmentManager.findFragmentByTag(FAVOURITES_FRAGMENT_TAG)
        if (pointerFragment == null || !pointerFragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, FavouritesFragment(), FAVOURITES_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        const val FAVOURITES_FRAGMENT_TAG = "favourites"
    }
}
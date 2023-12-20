package com.androidexpert.qurbanku_apps_skripsi.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityMapsUpdateLocationBinding
import com.androidexpert.qurbanku_apps_skripsi.databinding.CustomLocationPopUpBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsUpdateLocationActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.InfoWindowAdapter {
    private lateinit var binding: ActivityMapsUpdateLocationBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsUpdateLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.choose_masjid_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        latitude = intent.getDoubleExtra(Constanta.latitude, 0.0)
        longitude = intent.getDoubleExtra(Constanta.longitude, 0.0)
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.btnAddLocation.setOnClickListener {
            if (userViewModel.isUsingLocation.value == true) {
                val intent = Intent()
                intent.putExtra(Constanta.usingLocation, userViewModel.isUsingLocation.value)
                intent.putExtra(Constanta.latitude, userViewModel.latitude.value)
                intent.putExtra(Constanta.longitude, userViewModel.longitude.value)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this@MapsUpdateLocationActivity,
                    resources.getString(R.string.masjid_location_not_selected),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.setInfoWindowAdapter(this)
        val location = LatLng(latitude, longitude)
        // Add a marker to the map
        mMap.addMarker(MarkerOptions().position(location).title("Marker Title"))
        // Move camera to the marker position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))

        mMap.setOnInfoWindowClickListener { marker ->
            setLocation(marker.position.latitude, marker.position.longitude)
            marker.hideInfoWindow()
        }
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            it.latitude,
                            it.longitude
                        )
                    )
            )?.showInfoWindow()
        }
        mMap.setOnPoiClickListener {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            it.latLng.latitude,
                            it.latLng.longitude
                        )
                    )
            )?.showInfoWindow()
        }
        getMyLastLocation()
    }

    fun setLocation(latitude: Double, longitude: Double) {
        userViewModel.setLocation(true, latitude, longitude)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    setLocation(location.latitude, location.longitude)
                } else {
                    Toast.makeText(
                        this,
                        "Lokasi tidak ditemukan. Coba lagi!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val addressLayout = CustomLocationPopUpBinding.inflate(LayoutInflater.from(this))
        addressLayout.tvAddress.text = Helper.parseAddress(
            this,
            marker.position.latitude, marker.position.longitude
        )
        return addressLayout.root
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
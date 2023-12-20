package com.androidexpert.qurbanku_apps_skripsi.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityMapsListMasjidBinding
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardMasjidMapsViewBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah.DetailProfileMasjidActivity
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

class MapsListMasjidActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.InfoWindowAdapter {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsListMasjidBinding
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    val indonesianCoordinate = LatLng(-2.548926, 118.0148634)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsListMasjidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel =
            ViewModelProvider(
                this, ViewModelFactory.UserViewModelFactory(userRepository)
            )[UserViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.choose_masjid_by_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        userViewModel.getMasjidList()
        userViewModel.coordinateLocation.observe(this, {
            CameraUpdateFactory.newLatLngZoom(it, 3f)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 3f))
        })
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        userViewModel.listMasjidUser.observe(this, { masjidList ->
            setAllMasjidLocation(masjidList)
        })
        mMap.setInfoWindowAdapter(this@MapsListMasjidActivity)
        mMap.setOnInfoWindowClickListener { marker ->
            val masjid: MasjidUser = marker.tag as MasjidUser
            routeToDetailMasjid(masjid)
        }
        getMyLastLocation()
    }

    fun setAllMasjidLocation(masjidList: List<MasjidUser>) {
        for (masjid in masjidList) {
            mMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        masjid.user!!.latitude ?: 0.0,
                        masjid.user!!.longitude ?: 0.0
                    )
                )
            )?.tag = masjid
        }
    }

    fun routeToDetailMasjid(masjidData: MasjidUser) {
        val intentToDetail = Intent(this, DetailProfileMasjidActivity::class.java)
        intentToDetail.putExtra(Constanta.USER_DATA, masjidData)
        intentToDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentToDetail)
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
                    userViewModel.coordinateLocation.postValue(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )
                } else {
                    userViewModel.coordinateLocation.postValue(indonesianCoordinate)
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
        //do nothing
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val cardView = CardMasjidMapsViewBinding.inflate(LayoutInflater.from(this))
        val masjidData: MasjidUser = marker.tag as MasjidUser
        val masjid = masjidData.user
        val animalList = masjidData.listAnimal
        cardView.tvMasjidName.text = resources.getString(R.string.name_masjid_value, masjid!!.name)
        cardView.tvAddress.text = Helper.parseCompleteAddress(this, masjid.latitude!!, masjid.longitude!!)
        if (!animalList.isNullOrEmpty()) {
            var availableAnimal = 0
            animalList.forEach { animal ->
                if (animal.jointVentureAmount - (animal.idShohibulQurbaniList?.size ?: 0) > 0)
                    availableAnimal += 1
            }
            cardView.tvAvailableAnimalAmount.text = availableAnimal.toString()
        }
        return cardView.root
    }
    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }
}
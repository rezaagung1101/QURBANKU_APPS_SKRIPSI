package com.androidexpert.qurbanku_apps_skripsi.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityMapsListMasjidBinding
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardMosqueMapsViewBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah.DetailProfileMasjidActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsListMasjidActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsListMasjidBinding
    private val mapsViewModel: MapsViewModel by viewModels() //harusnya userviewModel
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
        supportActionBar?.title = resources.getString(R.string.choose_masjid_by_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        Add a marker in Indonesia and move the camera
        val indonesia = LatLng(-2.548926, 118.0148634)
        mMap.addMarker(MarkerOptions().position(indonesia).title("Marker di Indonesia"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesia))

        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
//        mapsViewModel.storyList.observe(this, {
//            setStoryList(it)
//        })
        mMap.setInfoWindowAdapter(this@MapsListMasjidActivity)
        mMap.setOnInfoWindowClickListener { marker ->
//            val masjid: User = marker.tag as User
//            routeToDetailMasjid(story)
            routeToDetailMasjid()
        }
        getMyLastLocation()
//        mapsViewModel.loadStoryLocationData(
//            this
//        )
//        mapsViewModel.coordinateLocation.observe(this, {
//            CameraUpdateFactory.newLatLngZoom(it, 3f)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 3f))
//        })
    }

    fun setAllMasjidLocation(){ //kasih parameter list user
//        for (masjid in masjidList) {
//            mMap.addMarker(
//                MarkerOptions().position(
//                    LatLng(
//                        masjid.lat ?: 0.0,
//                        masjid.lon ?: 0.0
//                    )
//                )
//            )?.tag = masjid
//        }
    }

    fun routeToDetailMasjid(){
        val intentToDetail = Intent(this, DetailProfileMasjidActivity::class.java)
//        intentToDetail.putExtra("NAME", story.name)
//        intentToDetail.putExtra("CREATEDAT", Helper.getUploadStoryTime(story.createdAt))
//        intentToDetail.putExtra("DESCRIPTION", story.description)
//        intentToDetail.putExtra("PHOTOURL", story.photoUrl)
//        intentToDetail.putExtra("LATITUDE", story.lat)
//        intentToDetail.putExtra("LONGITUDE", story.lon)
        intentToDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentToDetail)
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getMyLastLocation(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true
//            mapsViewModel.isLoading.observe(this, {
//                showLoading(it)
//            })
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    mapsViewModel.coordinateLocation.postValue(
//                        LatLng(
//                            location.latitude,
//                            location.longitude
//                        )
//                    )
//                } else {
//                    mapsViewModel.coordinateLocation.postValue(indonesianCoordinate)
//                }
//            }
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
        val cardView = CardMosqueMapsViewBinding.inflate(LayoutInflater.from(this))
//        val story: Story = marker.tag as Story
//        cardView.tvItemName.text = story.name
//        cardView.imgItemPhoto.setImageDrawable(resources.getDrawable(R.drawable.avatar))
//        cardView.imgItemStory.setImageBitmap(Helper.bitmapFromURL(this, story.photoUrl))
//        cardView.tvDescription.text = story.description
        return cardView.root
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }
}
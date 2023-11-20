package com.androidexpert.qurbanku_apps_skripsi.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.androidexpert.qurbanku_apps_skripsi.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker

class MapsPickLocationActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_pick_location)
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    override fun getInfoWindow(p0: Marker): View? {
        TODO("Not yet implemented")
    }
}
package com.androidexpert.qurbanku_apps_skripsi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityImageDisplayBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.bumptech.glide.Glide

class ImageDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.fully_image_display)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val imageUrl = intent.getStringExtra(Constanta.photoUrl)
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivLoader)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}
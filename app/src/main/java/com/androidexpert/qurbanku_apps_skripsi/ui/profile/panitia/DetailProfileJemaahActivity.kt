package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileJemaahBinding

class DetailProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileJemaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailProfileJemaahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_jemaah_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun setupInformation(){

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
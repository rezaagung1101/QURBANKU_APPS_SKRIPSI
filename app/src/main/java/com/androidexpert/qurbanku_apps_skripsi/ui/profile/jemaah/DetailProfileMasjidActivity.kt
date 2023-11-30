package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileMasjidBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah.DetailJemaahAnimalActivity

class DetailProfileMasjidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileMasjidBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileMasjidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title=resources.getString(R.string.profile_masjid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvAnimalData.setOnClickListener {//JANGAN LUPA DIHAPUS
            val intent = Intent(this, DetailJemaahAnimalActivity::class.java)
            //kirim data
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setupInformation(){

    }
}
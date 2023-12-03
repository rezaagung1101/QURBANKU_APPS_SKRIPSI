package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileMasjidBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah.DetailJemaahAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class DetailProfileMasjidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileMasjidBinding
    private var masjidData = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileMasjidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        masjidData = intent.getParcelableExtra<User>(Constanta.USER_DATA) as User
        supportActionBar?.title = resources.getString(R.string.profile_masjid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvAnimalData.setOnClickListener {//JANGAN LUPA DIHAPUS
            val intent = Intent(this, DetailJemaahAnimalActivity::class.java)
            //kirim data
            startActivity(intent)
        }
        setupInformation(masjidData)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setupInformation(masjidData: User) {
        binding.apply {
            with(masjidData) {
                tvMasjidName.text = resources.getString(R.string.name_masjid_value, name)
                tvEmailValue.text = email
                tvHeadValue.text = headName
                tvContactPersonValue.text = phoneNumber
                tvAccountNumberValue.text = bankAccountNumber
                tvBankValue.text = bankName
                tvAccountNameValue.text = bankAccountName
                tvAddressValue.text = Helper.parseCompleteAddress(
                    this@DetailProfileMasjidActivity,
                    latitude!!,
                    longitude!!
                )
                tvAddressValue.paintFlags = tvAddressValue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                tvAddressValue.setOnClickListener {
                    openGmapsByLocation(latitude, longitude)
                }
            }
        }
    }

    fun openGmapsByLocation(latitude: Double, longitude: Double){
        val location = "${latitude},${longitude}"
        val gmmIntentUri = Uri.parse("geo:0,0?q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager).let {
            startActivity(mapIntent)
        }
    }
}
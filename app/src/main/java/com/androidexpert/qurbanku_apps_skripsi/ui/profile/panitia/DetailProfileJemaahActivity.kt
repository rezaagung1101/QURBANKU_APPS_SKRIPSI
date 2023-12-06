package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta

class DetailProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileJemaahBinding
    private var jemaahData = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailProfileJemaahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_jemaah_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        jemaahData =  intent.getParcelableExtra<User>(Constanta.USER_DATA) as User
        setupInformation(jemaahData)
    }
    fun setupInformation(jemaahData: User){
        binding.apply {
            tvNameValue.text = jemaahData.name
            tvAddressValue.text = jemaahData.address
            tvEmailValue.text = jemaahData.email
            tvHeadValue.text = jemaahData.headName
            tvPhoneNumberValue.text = jemaahData.phoneNumber
//            tvQurbaniAmountValue.text
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
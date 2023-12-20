package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta

class DetailProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileJemaahBinding
    private var jemaahData = User()
    private val userRepository = UserRepository()
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailProfileJemaahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.detail_jemaah_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        jemaahData = intent.getParcelableExtra<User>(Constanta.USER_DATA) as User
        setupInformation(jemaahData)
    }

    fun setupInformation(jemaahData: User) {
        binding.apply {
            tvNameValue.text = jemaahData.name
            tvAddressValue.text = jemaahData.address
            tvEmailValue.text = if (jemaahData.email.length > 0) jemaahData.email
            else "-"
            tvHeadValue.text = jemaahData.headName
            tvPhoneNumberValue.text = jemaahData.phoneNumber
            if (jemaahData.email.length > 0) {
                userViewModel.getQurbaniAmount(jemaahData.uid)
                userViewModel.qurbaniAmount.observe(this@DetailProfileJemaahActivity) {
                    tvQurbaniAmountValue.text = it.toString()
                }
            } else tvQurbaniAmountValue.text = "-"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.androidexpert.qurbanku_apps_skripsi.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivitySplashScreenBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.main.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.main.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPreference : UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        userPreference = UserPreference(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        val delay: Long = 1500
        Handler(Looper.getMainLooper()).postDelayed({
            if (userPreference.isLogin()) {
                if (userPreference.isAdmin())
                    startActivity(Intent(this, MainPanitiaActivity::class.java))
                else
                    startActivity(Intent(this, MainJemaahActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }, delay)
    }
}
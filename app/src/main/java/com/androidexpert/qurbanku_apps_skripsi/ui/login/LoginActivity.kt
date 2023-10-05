package com.androidexpert.qurbanku_apps_skripsi.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityLoginBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.login.jemaah.LoginJemaahFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.login.panitia.LoginPanitiaFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isPanitia : Boolean = intent.getBooleanExtra("IS_PANITIA", true)
        val fragmentManager = supportFragmentManager
        val panitiaFragment = LoginPanitiaFragment()
        val jemaahFragment = LoginJemaahFragment()
        if (isPanitia==true) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, panitiaFragment, LoginPanitiaFragment::class.java.simpleName)
                .commit()
        }
        else{
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, jemaahFragment, LoginJemaahFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
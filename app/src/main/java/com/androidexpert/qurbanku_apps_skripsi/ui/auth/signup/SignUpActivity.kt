package com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivitySignUpBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.jemaah.SignUpJemaahFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.panitia.SignUpPanitiaFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isPanitia: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        isPanitia = intent.getBooleanExtra(Constanta.isPanitia, true)
        val fragmentManager = supportFragmentManager
        val panitiaFragment = SignUpPanitiaFragment()
        val jemaahFragment = SignUpJemaahFragment()
        if (isPanitia == true) {
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.frame_container,
                    panitiaFragment,
                    SignUpPanitiaFragment::class.java.simpleName
                )
                .commit()
        } else {
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.frame_container,
                    jemaahFragment,
                    SignUpJemaahFragment::class.java.simpleName
                )
                .commit()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(Constanta.isPanitia, isPanitia)
        startActivity(intent)
        finish()
    }

}
package com.androidexpert.qurbanku_apps_skripsi.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivitySignUpBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.login.jemaah.LoginJemaahFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.login.panitia.LoginPanitiaFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.jemaah.SignUpJemaahFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.panitia.SignUpPanitiaFragment
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isPanitia: Boolean = intent.getBooleanExtra(isPanitia, true)
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
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    companion object{
        val isPanitia: String = "IS_PANITIA"
    }

}
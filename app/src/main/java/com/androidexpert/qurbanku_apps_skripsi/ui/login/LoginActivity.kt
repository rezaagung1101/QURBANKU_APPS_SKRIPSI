package com.androidexpert.qurbanku_apps_skripsi.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityLoginBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isPanitia: Boolean = intent.getBooleanExtra(isPanitia, true)
        if (isPanitia != true) {
            binding.ivBanner.setImageDrawable(resources.getDrawable(R.drawable.banner_jemaah))
            binding.tvLoginQuote.text = resources.getString(R.string.login_jemaah_quote)
            binding.tvSignUpQuestion.text = resources.getString(R.string.signUp_question_jemaah)
            binding.etEmailLayout.hint = resources.getString(R.string.email)
            binding.tvActor.text = resources.getString(R.string.jemaah_allcaps)
            binding.tvSignUpQuestion.text = resources.getString(R.string.signUp_question_jemaah)
            binding.btnLogin.setOnClickListener {
                //login jemaah
            }
        } else {
            binding.btnLogin.setOnClickListener {
                //login panitia
                loginPanitia()

            }
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(SignUpActivity.isPanitia, isPanitia)
            startActivity(intent)
        }
    }

    private fun loginPanitia() {
        startActivity(Intent(this, MainPanitiaActivity::class.java))
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    companion object {
        val isPanitia: String = "IS_PANITIA"
    }
}
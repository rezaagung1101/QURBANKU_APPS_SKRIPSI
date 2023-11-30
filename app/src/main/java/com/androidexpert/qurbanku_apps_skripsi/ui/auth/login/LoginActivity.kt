package com.androidexpert.qurbanku_apps_skripsi.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityLoginBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isPanitia: Boolean = intent.getBooleanExtra(Constanta.isPanitia, true)
        if (isPanitia != true) {
            binding.ivBanner.setImageDrawable(resources.getDrawable(R.drawable.banner_jemaah))
            binding.tvLoginQuote.text = resources.getString(R.string.login_jemaah_quote)
            binding.tvSignUpQuestion.text = resources.getString(R.string.signUp_question_jemaah)
            binding.etEmailLayout.hint = resources.getString(R.string.email)
            binding.tvActor.text = resources.getString(R.string.jemaah_allcaps)
            binding.tvSignUpQuestion.text = resources.getString(R.string.signUp_question_jemaah)
            binding.btnLogin.setOnClickListener {
                //login jemaah
                loginJemaah()
            }
        } else {
            binding.btnLogin.setOnClickListener {
                //login panitia
                loginPanitia()

            }
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(Constanta.isPanitia, isPanitia)
            startActivity(intent)
        }
    }

    fun loginJemaah() {
        //if success
        val title = resources.getString(R.string.login_success_title, "temp name")
        val message = resources.getString(R.string.login_success_jemaah_message)
        DialogUtils.showNotificationDialog(this, title, message, {
            startActivity(Intent(this, MainJemaahActivity::class.java)) }
        )
    }

    fun loginPanitia() {
        //if success
        val title = resources.getString(R.string.login_success_title, "temp name")
        val message = resources.getString(R.string.login_success_panitia_message)
        DialogUtils.showNotificationDialog(this, title, message, {
            startActivity(Intent(this, MainPanitiaActivity::class.java)) }
        )
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

}
package com.androidexpert.qurbanku_apps_skripsi.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityLoginBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setError
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setupTextWatcher

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
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (validation(email, password)) {
                login(isPanitia, email, password)
            }
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(Constanta.isPanitia, isPanitia)
            startActivity(intent)
        }
    }

    fun login(isPanitia: Boolean, email: String, password: String) {
        if (isPanitia) {
            //call viewModel
            //if success
            val title = resources.getString(R.string.login_success_panitia_title, email)
            val message = resources.getString(R.string.login_success_panitia_message)
            DialogUtils.showNotificationDialog(this, title, message, {
                startActivity(Intent(this, MainPanitiaActivity::class.java))
            })
        } else {
            //call viewModel
            //if success
            val title = resources.getString(R.string.login_success_jemaah_title, email)
            val message = resources.getString(R.string.login_success_jemaah_message)
            DialogUtils.showNotificationDialog(this, title, message, {
                startActivity(Intent(this, MainJemaahActivity::class.java))
            }
            )
        }
    }

    fun validation(email: String, password: String): Boolean {
        var isValid = false
        binding.apply {
            val editTextPairs = listOf(
                etEmail to etEmailLayout,
                etPassword to etPasswordLayout
            )
            editTextPairs.forEach { (editText, layout) ->
                setupTextWatcher(editText, layout)
            }
            val isEmailValid = email.isNotEmpty() && Helper.emailValidation(email)
            val isPasswordValid = password.isNotEmpty() && Helper.passwordValidation(password)
            if (isEmailValid && isPasswordValid) {
                isValid = true
            } else if (!isEmailValid) {
                setError(
                    this@LoginActivity,
                    etEmail,
                    etEmailLayout,
                    resources.getString(R.string.email_invalid)
                )
            }
            if (!isPasswordValid) {
                setError(
                    this@LoginActivity,
                    etPassword,
                    etPasswordLayout,
                    resources.getString(R.string.password_minimum)
                )
            }
        }
        return isValid
    }


    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

}
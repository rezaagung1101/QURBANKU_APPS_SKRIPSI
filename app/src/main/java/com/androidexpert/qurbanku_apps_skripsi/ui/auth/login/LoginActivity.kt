package com.androidexpert.qurbanku_apps_skripsi.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityLoginBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.main.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.main.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setError
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setupTextWatcher
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        userPreference = UserPreference(this)
        authViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AuthViewModelFactory(authRepository)
        )[AuthViewModel::class.java]
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
        authViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    fun login(isAdmin: Boolean, email: String, password: String) {
        authViewModel.login(email, password)
        authViewModel.loginResult.observe(this) { isSuccess ->
            if (isSuccess) {
                authViewModel.user.observe(this) { user ->
                    if (user != null && user.admin == isAdmin) {
                        if (user.admin == true) {
                            userPreference.savePanitiaPreference(user)
                            showSuccessDialog(user.admin, user.name)
                        } else {
                            userPreference.saveJemaahPreference(user)
                            showSuccessDialog(user.admin, user.name)
                        }
                    } else if (user != null && user.admin != isAdmin) showErrorDialog()
                }
            } else {
                showErrorDialog()
            }
        }

    }
    fun showSuccessDialog(isAdmin: Boolean, name: String) {
        val titleResId = if (isAdmin) R.string.login_success_panitia_title else R.string.login_success_jemaah_title
        val messageResId = if (isAdmin) R.string.login_success_panitia_message else R.string.login_success_jemaah_message

        val title = resources.getString(titleResId, name)
        val message = resources.getString(messageResId)

        DialogUtils.showNotificationDialog(this, title, message) {
            val destinationActivity = if (isAdmin) MainPanitiaActivity::class.java else MainJemaahActivity::class.java
            startActivity(Intent(this, destinationActivity))
            finish()
        }
    }

    fun showErrorDialog() {
        authViewModel.loginResult.removeObservers(this)
        val title = resources.getString(R.string.login_failed_title)
        val message = resources.getString(R.string.login_failed_message)
        DialogUtils.showNotificationDialog(this, title, message, {})
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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

}
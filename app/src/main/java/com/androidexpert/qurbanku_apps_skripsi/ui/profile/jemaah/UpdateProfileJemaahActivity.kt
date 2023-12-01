package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class UpdateProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileJemaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileJemaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setupInformation()
        binding.btnSaveUpdate.setOnClickListener {
            binding.apply {
                val title = resources.getString(R.string.update_profile)
                val message = resources.getString(R.string.update_profile_message)
                val email = etEmail.text.toString()
                val name = etName.text.toString()
                val headName = etHeadFamilyName.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val password = etPassword.text.toString()
                val address = etAddress.text.toString()
                if (validation(email, phoneNumber, name, address, headName, password))
                    DialogUtils.showConfirmationDialog(
                        this@UpdateProfileJemaahActivity,
                        title,
                        message,
                        {updateProfile(email, phoneNumber, name, address, headName, password)}
                    )
            }
        }
    }

    fun updateProfile(
        email: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
        password: String,
    ) {
        val title = resources.getString(R.string.announcement)
        val message = resources.getString(R.string.update_profile_success)
        DialogUtils.showNotificationDialog(this, title, message, ::onBackPressed)
    }

    fun validation(
        email: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
        password: String,
    ): Boolean {
        var isValid = false
        binding.apply {
            val isEmailValid = email.isNotEmpty() && Helper.emailValidation(email)
            val isPhoneNumberValid = phoneNumber.isNotEmpty()
            val isNameValid = name.isNotEmpty()
            val isAddressValid = address.isNotEmpty()
            val isHeadNameValid = headName.isNotEmpty()
            val isPasswordValid = password.isNotEmpty() && Helper.passwordValidation(password)
            val editTextPairs = listOf(
                etEmail to etEmailLayout,
                etPhoneNumber to etPhoneNumberLayout,
                etName to etNameLayout,
                etAddress to etAddressLayout,
                etHeadFamilyName to etHeadFamilyNameLayout,
                etPassword to etPasswordLayout,
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isEmailValid && isPhoneNumberValid && isNameValid && isAddressValid && isHeadNameValid && isPasswordValid) {
                isValid = true
            }

            val validationList = listOf(
                isEmailValid to resources.getString(R.string.email_invalid),
                isPhoneNumberValid to null,
                isNameValid to null,
                isAddressValid to null,
                isHeadNameValid to null,
                isPasswordValid to resources.getString(R.string.password_minimum)
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(
                            this@UpdateProfileJemaahActivity,
                            editText,
                            layout,
                            errorMessage
                        )
                    }
                }
            }
        }
        return isValid
    }

    fun setupInformation() {
        val guide = resources.getStringArray(R.array.note_jemaah_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
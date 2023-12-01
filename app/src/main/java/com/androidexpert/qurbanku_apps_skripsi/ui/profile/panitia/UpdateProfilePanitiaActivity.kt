package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsPickLocationActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class UpdateProfilePanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfilePanitiaBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var usingLocation: Boolean? = false
    private val launcerIntentLocation = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { result ->
                usingLocation = result.getBooleanExtra(Constanta.usingLocation, false)
//                latitude = result.getDoubleExtra(Constanta.latitude, 0.0)
//                longitude = result.getDoubleExtra(Constanta.longitude, 0.0)
                authViewModel.apply {
                    isUsingLocation.postValue(usingLocation)
                    val tempLatitude = result.getDoubleExtra(Constanta.latitude, 0.0)
                    val tempLongitude = result.getDoubleExtra(Constanta.longitude, 0.0)
                    latitude.postValue(tempLatitude)
                    longitude.postValue(tempLongitude)
                    setupInformation(tempLatitude, tempLongitude)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfilePanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setupInformation(null, null)
        binding.btnAddAddress.setOnClickListener {
            pickLocation()
        }
        binding.btnSaveUpdate.setOnClickListener {
            binding.apply {
                val email = etEmail.text.toString()
                val name = etName.text.toString()
                val headName = etNameHeadTakmir.text.toString()
                val phoneNumber = etContactPersonNumber.text.toString()
                val password = etPassword.text.toString()
                val accountNumber = etAccountNumber.text.toString()
                val bankName = etBankName.text.toString()
                val accountName = etAccountName.text.toString()
                //if success
                if (validation(
                        email,
                        name,
                        headName,
                        phoneNumber,
                        password,
                        accountNumber,
                        bankName,
                        accountName
                    )
                ) {
                    val title= resources.getString(R.string.update_profile)
                    val message= resources.getString(R.string.update_profile_message)
                    DialogUtils.showConfirmationDialog(this@UpdateProfilePanitiaActivity, title, message, {
                        updateProfile(
                            email,
                            name,
                            headName,
                            phoneNumber,
                            password,
                            latitude,
                            longitude,
                            accountNumber,
                            bankName,
                            accountName
                        )
                    })
                }
            }
        }
    }
    fun updateProfile(
        email: String,
        name: String,
        headName: String,
        phoneNumber: String,
        password: String,
        latitude: Double,
        longitude: Double,
        accountNumber: String,
        bankName: String,
        accountName: String,
    ){
        //call viewModel

        //ifSuccess
        val title = resources.getString(R.string.announcement)
        val message = resources.getString(R.string.update_profile_success)
        DialogUtils.showNotificationDialog(this, title, message, ::onBackPressed)
    }

    fun pickLocation() {
        val intent = Intent(this, MapsPickLocationActivity::class.java)
        launcerIntentLocation.launch(intent)
    }

    fun validation( //not using isUsingLocation like in sign up
        email: String,
        name: String,
        headName: String,
        phoneNumber: String,
        password: String,
        accountNumber: String,
        bankName: String,
        accountName: String,
    ): Boolean {
        var isValid = false
        binding.apply {
            val isEmailValid = email.isNotEmpty() && Helper.emailValidation(email)
            val isPasswordValid = password.isNotEmpty() && Helper.passwordValidation(password)
            val isNameValid = name.isNotEmpty()
            val isHeadNameValid = headName.isNotEmpty()
            val isPhoneNumberValid = phoneNumber.isNotEmpty()
            val isAccountNumberValid = accountNumber.isNotEmpty()
            val isBankNameValid = bankName.isNotEmpty()
            val isAccountNameValid = accountName.isNotEmpty()
            val editTextPairs = listOf(
                etEmail to etEmailLayout,
                etPassword to etPasswordLayout,
                etName to etNameLayout,
                etNameHeadTakmir to etNameHeadTakmirLayout,
                etContactPersonNumber to etContactPersonNumberLayout,
                etAccountNumber to etAccountNumberLayout,
                etBankName to etBankNameLayout,
                etAccountName to etAccountNameLayout
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isEmailValid && isNameValid && isHeadNameValid && isPhoneNumberValid && isAccountNumberValid && isBankNameValid && isAccountNameValid && isPasswordValid) {
                isValid = true
            }

            val validationList = listOf(
                isEmailValid to resources.getString(R.string.email_invalid),
                isPasswordValid to resources.getString(R.string.password_minimum),
                isNameValid to null,
                isHeadNameValid to null,
                isPhoneNumberValid to null,
                isAccountNumberValid to null,
                isBankNameValid to null,
                isAccountNameValid to null
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(this@UpdateProfilePanitiaActivity, editText, layout, errorMessage)
                    }
                }
            }

        }
        return isValid
    }

    fun setupInformation(latitude: Double?, longitude: Double?) {
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
        if (latitude != null && longitude != null) {
            authViewModel.isUsingLocation.observe(this, { usingLocation ->
                if (usingLocation == true) binding.btnAddAddress.text =
                    Helper.parseAddress(this, latitude, longitude)
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
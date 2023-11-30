package com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.panitia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsPickLocationActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setupTextWatcher
import com.google.android.material.textfield.TextInputLayout

class SignUpPanitiaFragment : Fragment() {
    private lateinit var binding: FragmentSignUpPanitiaBinding
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
                    setupInfromation(tempLatitude, tempLongitude)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpPanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupInfromation(null, null)
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddAddress.setOnClickListener {
            pickLocation()
        }
        binding.btnSignUp.setOnClickListener {
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
                        usingLocation ?: false,
                        accountNumber,
                        bankName,
                        accountName
                    )
                ) {
                    val title = resources.getString(R.string.signup)
                    val message = resources.getString(R.string.signup_message)
                    DialogUtils.showConfirmationDialog(requireContext(), title, message, {
                        signUp(
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
        binding.btnLogin.setOnClickListener {
            login()
        }

    }

    fun signUp(
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
    ) {
        //if success
        val title = resources.getString(R.string.signup_success_title)
        val message = resources.getString(R.string.signup_success_message)
        DialogUtils.showNotificationDialog(requireContext(), title, message, ::login)
    }


    fun login() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(Constanta.isPanitia, true)
        startActivity(intent)
        requireActivity().finish()
    }

    fun pickLocation() {
        val intent = Intent(requireContext(), MapsPickLocationActivity::class.java)
        launcerIntentLocation.launch(intent)
    }

    fun validation(
        email: String,
        name: String,
        headName: String,
        phoneNumber: String,
        password: String,
        usingLocation: Boolean,
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
            val isUsingLocation = usingLocation
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
                setupTextWatcher(editText, layout)
            }

            if (isEmailValid && isNameValid && isHeadNameValid && isPhoneNumberValid && isUsingLocation && isAccountNumberValid && isBankNameValid && isAccountNameValid && isPasswordValid) {
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
                        Helper.setError(requireContext(), editText, layout, errorMessage)
                    }
                }
            }

            if (!isUsingLocation) Toast.makeText(
                requireContext(),
                resources.getString(R.string.masjid_location_not_selected),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }

    fun setupInfromation(latitude: Double?, longitude: Double?) {
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
        if (latitude != null && longitude != null) {
            authViewModel.isUsingLocation.observe(viewLifecycleOwner, { usingLocation ->
                if (usingLocation == true) binding.btnAddAddress.text =
                    Helper.parseAddress(requireContext(), latitude, longitude)
            })
        }
    }

}
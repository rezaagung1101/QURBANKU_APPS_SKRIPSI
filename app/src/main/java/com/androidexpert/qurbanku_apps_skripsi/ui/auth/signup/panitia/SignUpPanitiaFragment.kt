package com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.panitia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsPickLocationActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper.setupTextWatcher

class SignUpPanitiaFragment : Fragment() {
    private lateinit var binding: FragmentSignUpPanitiaBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private var usingLocation: Boolean? = false
    private val launcerIntentLocation = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { result ->
                usingLocation = result.getBooleanExtra(Constanta.usingLocation, false)
                val tempLatitude = result.getDoubleExtra(Constanta.latitude, 0.0)
                val tempLongitude = result.getDoubleExtra(Constanta.longitude, 0.0)
                authViewModel.setLocation(usingLocation!!, tempLatitude, tempLongitude)
                setupInfromation(tempLatitude, tempLongitude)
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
        authViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AuthViewModelFactory(authRepository)
        )[AuthViewModel::class.java]
        this.setupInfromation(null, null)
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
                val latitude = authViewModel.latitude.value!!.toDouble()
                val longitude = authViewModel.longitude.value!!.toDouble()
                val password = etPassword.text.toString()
                val accountNumber = etAccountNumber.text.toString()
                val bankName = etBankName.text.toString()
                val accountName = etAccountName.text.toString()
                val user = User(
                    uid = "",
                    email = email,
                    headName = headName,
                    name = name,
                    phoneNumber = phoneNumber,
                    admin = true,
                    address = null,
                    bankAccountName = accountName,
                    latitude = latitude,
                    longitude = longitude,
                    bankName = bankName,
                    bankAccountNumber = accountNumber
                )
                //if success
                if (validation(user, password, usingLocation ?: false)
                ) {
                    val title = resources.getString(R.string.signup)
                    val message = resources.getString(R.string.signup_message)
                    DialogUtils.showConfirmationDialog(requireContext(), title, message, {
                        signUp(user, password)
                    })
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            login()
        }

    }

    fun signUp(user: User, password: String) {
        authViewModel.signUpUser(user, password)
        // Observe the registration result in the ViewModel and handle UI accordingly
        authViewModel.registrationResult.observe(viewLifecycleOwner, { isSuccess ->
            var title = ""
            var message = ""
            if (isSuccess == true) {
                title = resources.getString(R.string.signup_success_title)
                message = resources.getString(R.string.signup_success_message)
                DialogUtils.showNotificationDialog(requireContext(), title, message, ::login)
            } else {
                title = resources.getString(R.string.signup_failed_title)
                message = resources.getString(R.string.signup_failed_message)
                DialogUtils.showNotificationDialog(requireContext(), title, message, {})
            }
        })
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

    fun validation(user: User, password: String, usingLocation: Boolean): Boolean {
        var isValid = false
        binding.apply {
            val isEmailValid = user.email.isNotEmpty() && Helper.emailValidation(user.email)
            val isPasswordValid = password.isNotEmpty() && Helper.passwordValidation(password)
            val isNameValid = user.name.isNotEmpty()
            val isHeadNameValid = user.headName.isNotEmpty()
            val isPhoneNumberValid = user.phoneNumber.isNotEmpty()
            val isUsingLocation = usingLocation
            val isAccountNumberValid = user.bankAccountNumber?.isNotEmpty()
            val isBankNameValid = user.bankName?.isNotEmpty()
            val isAccountNameValid = user.bankAccountName?.isNotEmpty()
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

            if (isEmailValid && isNameValid && isHeadNameValid && isPhoneNumberValid && isUsingLocation && isAccountNumberValid!! && isBankNameValid!! && isAccountNameValid!! && isPasswordValid) {
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
                if (!isValid!!) {
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
        authViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
        if (latitude != null && longitude != null) {
            authViewModel.isUsingLocation.observe(viewLifecycleOwner, { usingLocation ->
                if (usingLocation == true) binding.btnAddAddress.text =
                    Helper.parseAddress(requireContext(), latitude, longitude)
            })
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        authViewModel.registrationResult.removeObservers(viewLifecycleOwner)
        authViewModel.isLoading.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

}


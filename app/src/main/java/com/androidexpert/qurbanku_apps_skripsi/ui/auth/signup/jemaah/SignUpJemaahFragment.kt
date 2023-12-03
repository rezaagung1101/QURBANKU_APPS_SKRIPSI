package com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.jemaah

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class SignUpJemaahFragment : Fragment() {
    private lateinit var binding: FragmentSignUpJemaahBinding
    private val authRepository = AuthRepository()
    private lateinit var authViewModel: AuthViewModel
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignUpJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AuthViewModelFactory(authRepository)
        )[AuthViewModel::class.java]
        this.setupInformation()
        binding.apply {

            btnLogin.setOnClickListener {
                login()
            }
            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val name = etName.text.toString()
                val headName = etHeadFamilyName.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val password = etPassword.text.toString()
                val address = etAddress.text.toString()
                val user = User(
                    uid = "",
                    email = email,
                    headName = headName,
                    name = name,
                    phoneNumber = phoneNumber,
                    admin = false,
                    address = address,
                    bankAccountName = null,
                    latitude = null,
                    longitude = null,
                    bankName = null,
                    bankAccountNumber = null
                )
                val title = resources.getString(R.string.signup)
                val message = resources.getString(R.string.signup_message)
                if (validation(user, password))
                    DialogUtils.showConfirmationDialog(requireContext(), title, message, {
                        signUp(user, password)
                    })
            }
        }
    }

    fun signUp(user: User, password: String) {

        //call viewModels
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

    fun validation(
        user: User,
        password: String,
    ): Boolean {
        var isValid = false
        binding.apply {
            val isEmailValid = user.email.isNotEmpty() && Helper.emailValidation(user.email)
            val isPhoneNumberValid = user.phoneNumber.isNotEmpty()
            val isNameValid = user.name.isNotEmpty()
            val isAddressValid = user.address?.isNotEmpty()
            val isHeadNameValid = user.headName.isNotEmpty()
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

            if (isEmailValid && isPhoneNumberValid && isNameValid && isAddressValid!! && isHeadNameValid && isPasswordValid) {
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
                if (!isValid!!) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(requireContext(), editText, layout, errorMessage)
                    }
                }
            }
        }
        return isValid
    }

    fun login() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(Constanta.isPanitia, false)
        startActivity(intent)
        requireActivity().finish()
    }

    fun setupInformation() {
        authViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
        val stringArray = resources.getStringArray(R.array.note_jemaah_signUp)
        val text = stringArray.joinToString("\n")
        binding.tvNoteValue.text = text

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
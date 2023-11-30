package com.androidexpert.qurbanku_apps_skripsi.ui.auth.signup.jemaah

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class SignUpJemaahFragment : Fragment() {
    private lateinit var binding: FragmentSignUpJemaahBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignUpJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val title = resources.getString(R.string.signup)
                val message = resources.getString(R.string.signup_message)
                if (validation(email, phoneNumber, name, address, headName, password))
                    DialogUtils.showConfirmationDialog(requireContext(), title, message, {
                        signUp(email, phoneNumber, name, address, headName, password)
                    })
            }
        }
    }

    fun signUp(
        email: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
        password: String,
    ) {
        //call viewModels

        //if success
        val title = resources.getString(R.string.signup_success_title)
        val message = resources.getString(R.string.signup_success_message)
        DialogUtils.showNotificationDialog(requireContext(), title, message, ::login)
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
        val stringArray = resources.getStringArray(R.array.note_jemaah_signUp)
        val text = stringArray.joinToString("\n")
        binding.tvNoteValue.text = text

    }



}
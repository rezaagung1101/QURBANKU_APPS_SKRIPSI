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
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnSignUp.setOnClickListener {
            val title = resources.getString(R.string.signup)
            val message = resources.getString(R.string.signup_message)
            DialogUtils.showConfirmationDialog(requireContext(), title, message, ::signUp)
        }
    }

    fun signUp(){
        //if success
        val title = resources.getString(R.string.signup_success_title)
        val message = resources.getString(R.string.signup_success_message)
        DialogUtils.showNotificationDialog(requireContext(), title, message, ::login)
    }
    fun login(){
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(Constanta.isPanitia, false)
        startActivity(intent)
        requireActivity().finish()
    }

    fun setupInformation(){
        val stringArray = resources.getStringArray(R.array.note_jemaah_signUp)
        val text = stringArray.joinToString("\n")
        binding.tvNoteValue.text = text

    }

}
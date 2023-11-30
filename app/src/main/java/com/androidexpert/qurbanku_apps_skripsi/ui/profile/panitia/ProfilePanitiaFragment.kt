package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta.isPanitia
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class ProfilePanitiaFragment : Fragment() {
    private lateinit var binding: FragmentProfilePanitiaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfilePanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            val title= resources.getString(R.string.logout)
            val message= resources.getString(R.string.logout_message)
            DialogUtils.showConfirmationDialog(requireContext(), title, message, ::logout)
        }
        binding.btnUpdateProfile.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfilePanitiaActivity::class.java))
        }

    }
    fun logout(){
        //hapus preference
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(isPanitia, true)
        startActivity(intent)
        requireActivity().finish()
    }
    fun setupInformation(){

    }

}
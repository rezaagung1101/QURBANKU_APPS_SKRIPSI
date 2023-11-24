package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

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
        Toast.makeText(
            requireContext(),
            resources.getString(
                R.string.logout
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun setupInformation(){

    }

}
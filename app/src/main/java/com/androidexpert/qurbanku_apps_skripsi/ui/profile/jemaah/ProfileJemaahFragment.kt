package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta.isPanitia
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class ProfileJemaahFragment : Fragment() {
    private lateinit var binding: FragmentProfileJemaahBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdateProfile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateProfileJemaahActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            val title= resources.getString(R.string.logout)
            val message= resources.getString(R.string.logout_message)
            DialogUtils.showConfirmationDialog(requireContext(), title, message, ::logout)
        }
    }

    fun getProfile() {

    }

    fun logout() {
        //hapus preference
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(isPanitia, false)
        startActivity(intent)
        requireActivity().finish()
    }

    fun setupInformation() {

    }
}
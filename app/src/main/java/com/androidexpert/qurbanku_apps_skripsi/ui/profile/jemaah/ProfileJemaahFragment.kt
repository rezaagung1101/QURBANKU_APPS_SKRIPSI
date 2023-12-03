package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta.isPanitia
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class ProfileJemaahFragment : Fragment() {
    private lateinit var binding: FragmentProfileJemaahBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        userPreference = UserPreference(requireContext())
        setupInformation(userPreference.getJemaahData())
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdateProfile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateProfileJemaahActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            val title = resources.getString(R.string.logout)
            val message = resources.getString(R.string.logout_message)
            DialogUtils.showConfirmationDialog(requireContext(), title, message, ::logout)
        }
    }

    fun getProfile() {

    }

    fun logout() {
        //hapus preference
        userViewModel.logout()
        userPreference.logout()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(isPanitia, false)
        startActivity(intent)
        requireActivity().finish()
    }

    fun setupInformation(user: User) {
        binding.apply {
            tvName.text = user.name
            tvEmailValue.text = user.email
            tvAddressValue.text = user.address
            tvPhoneNumberValue.text = user.phoneNumber
            tvHeadValue.text = user.headName
        }
    }
}
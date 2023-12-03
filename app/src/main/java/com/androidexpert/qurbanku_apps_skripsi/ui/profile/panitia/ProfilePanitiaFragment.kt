package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

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
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.welcome.WelcomeActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta.isPanitia
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class ProfilePanitiaFragment : Fragment() {
    private lateinit var binding: FragmentProfilePanitiaBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfilePanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        userPreference = UserPreference(requireContext())
        setupInformation(userPreference.getPanitiaData())
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
        userViewModel.logout()
        userPreference.logout()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(isPanitia, true)
        startActivity(intent)
        requireActivity().finish()
    }
    fun setupInformation(user: User){
        binding.apply {
            tvMasjidName.text = resources.getString(R.string.name_masjid_value, user.name)
            tvEmailValue.text = user.email
            tvAddressValue.text = Helper.parseCompleteAddress(requireContext(), user.latitude!!, user.longitude!!)
            tvContactPersonValue.text = user.phoneNumber
            tvHeadValue.text = user.headName
            tvBankValue.text = user.bankName
            tvAccountNameValue.text = user.bankAccountName
            tvAccountNumberValue.text = user.bankAccountNumber
        }
    }

}
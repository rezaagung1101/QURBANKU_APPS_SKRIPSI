package com.androidexpert.qurbanku_apps_skripsi.ui.signup.panitia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsPickLocationActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

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
                usingLocation = result.getBooleanExtra("USINGLOCATION", false)
//                latitude = result.getDoubleExtra(latitude_code, 0.0)
//                longitude = result.getDoubleExtra(longitude_code, 0.0)
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

        }

    }

    fun pickLocation() {
        val intent = Intent(requireContext(), MapsPickLocationActivity::class.java)
        launcerIntentLocation.launch(intent)
    }

    fun setupInfromation(latitude: Double?, longitude: Double?) {
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
        if (latitude != null && longitude != null) {
            authViewModel.isUsingLocation.observe(viewLifecycleOwner, { usingLocation ->
                if (usingLocation==true) binding.btnAddAddress.text = Helper.parseAddress(requireContext(), latitude, longitude)
            })
        }
    }

}
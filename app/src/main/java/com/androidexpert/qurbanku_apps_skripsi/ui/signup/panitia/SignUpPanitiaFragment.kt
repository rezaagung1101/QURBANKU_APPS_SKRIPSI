package com.androidexpert.qurbanku_apps_skripsi.ui.signup.panitia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia.AddAnimalDataActivity

class SignUpPanitiaFragment : Fragment(){
    private lateinit var binding: FragmentSignUpPanitiaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpPanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(requireContext(), AddAnimalDataActivity::class.java))
        }

    }

}
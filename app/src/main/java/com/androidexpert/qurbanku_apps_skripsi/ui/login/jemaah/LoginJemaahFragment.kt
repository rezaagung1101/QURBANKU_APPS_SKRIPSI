package com.androidexpert.qurbanku_apps_skripsi.ui.login.jemaah

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentLoginJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.SignUpActivity.Companion.isPanitia

class LoginJemaahFragment : Fragment() {
    private lateinit var binding: FragmentLoginJemaahBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            intent.putExtra(isPanitia, false)
            startActivity(intent)
        }
    }

}
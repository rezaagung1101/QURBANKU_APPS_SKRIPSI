package com.androidexpert.qurbanku_apps_skripsi.ui.login.panitia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentLoginPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.SignUpActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.signup.SignUpActivity.Companion.isPanitia

class LoginPanitiaFragment : Fragment() {

    private lateinit var binding: FragmentLoginPanitiaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginPanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            intent.putExtra(isPanitia, true)
            startActivity(intent)
        }
    }

}
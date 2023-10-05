package com.androidexpert.qurbanku_apps_skripsi.ui.signup.jemaah

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentSignUpJemaahBinding

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
        this.setupView()
    }

    fun setupView(){
        val stringArray = resources.getStringArray(R.array.note_jemaah_signUp)
        val text = stringArray.joinToString("\n")
        binding.tvNoteValue.text = text
    }

}
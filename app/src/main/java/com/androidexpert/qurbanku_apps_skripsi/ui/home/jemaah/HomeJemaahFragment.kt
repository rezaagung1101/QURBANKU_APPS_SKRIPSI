package com.androidexpert.qurbanku_apps_skripsi.ui.home.jemaah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentHomeJemaahBinding

class HomeJemaahFragment : Fragment() {
    private lateinit var binding: FragmentHomeJemaahBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
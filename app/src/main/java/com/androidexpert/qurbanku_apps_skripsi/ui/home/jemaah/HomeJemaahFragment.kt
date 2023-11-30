package com.androidexpert.qurbanku_apps_skripsi.ui.home.jemaah

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentHomeJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsListMasjidActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah.DetailProfileMasjidActivity

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
        binding.cardMasjidList.setOnClickListener { //JGN LUPA DIHAPUS
            val intent = Intent(requireContext(), DetailProfileMasjidActivity::class.java)
            //kirim data
            startActivity(intent)
        }
        binding.btnFindMasjidLocation.setOnClickListener {
            startActivity(Intent(requireContext(), MapsListMasjidActivity::class.java))
        }
    }
}
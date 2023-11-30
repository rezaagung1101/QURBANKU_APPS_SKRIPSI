package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentTransactionListJemaahBinding


class TransactionListJemaahFragment : Fragment() {
    private lateinit var binding: FragmentTransactionListJemaahBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTransactionListJemaahBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //FOR TEMPORARY
        binding.tvYourTransactionTitle.setOnClickListener {
            //Send the data
            val intent = Intent(requireContext(), DetailTransactionJemaahActivity::class.java)
            startActivity(intent)
        }
    }

    fun getTransactionListData(){

    }

    fun setupInformation(){

    }

}
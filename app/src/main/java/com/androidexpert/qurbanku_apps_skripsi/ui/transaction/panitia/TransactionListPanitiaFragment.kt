package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentTransactionListPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia.DetailProfileJemaahActivity

class TransactionListPanitiaFragment : Fragment() {

    private lateinit var binding: FragmentTransactionListPanitiaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionListPanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupInformation()
    }

    fun setupInformation(){
        val guide = resources.getStringArray(R.array.transaction_explanation_value).joinToString("\n")
        binding.tvTransactionExplanationValue.text = guide
        binding.tvTransactionListTitle.setOnClickListener {
            startActivity(Intent(requireContext(), DetailTransactionPanitiaActivity::class.java))
        }
        binding.tvTransactionExplanationTitle.setOnClickListener {
            startActivity(Intent(requireContext(), DetailProfileJemaahActivity::class.java))
        }
    }
}
package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.adapter.transaction.TransactionPanitiaAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentTransactionListPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.TransactionViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class TransactionListPanitiaFragment : Fragment() {

    private lateinit var binding: FragmentTransactionListPanitiaBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val transactionRepository = TransactionRepository()
    private lateinit var userPreferences: UserPreference

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
        userPreferences = UserPreference(requireContext())
        transactionViewModel = ViewModelProvider(this, ViewModelFactory.TransactionViewModelFactory(transactionRepository))[TransactionViewModel::class.java]
        getTransactionList(userPreferences.getUid()!!)
        binding.swipeRefresh.setOnRefreshListener {
            getTransactionList(userPreferences.getUid()!!)
            binding.swipeRefresh.isRefreshing = true
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
                binding.rvTransactionJemaahList.smoothScrollToPosition(0)
            }, 1000)
        }
        val guide = resources.getStringArray(R.array.transaction_explanation_value).joinToString("\n")
        binding.tvTransactionExplanationValue.text = guide
    }

    fun getTransactionList(uid: String){
        transactionViewModel.getTransactionList(uid, true)
        transactionViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        transactionViewModel.listTransactionDetail.observe(viewLifecycleOwner){
            if(it!=null){
                setupInformation(it)
            }
        }

    }
    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    fun setupInformation(transactionList: List<TransactionDetail>){
        var arrayListTransaction = arrayListOf<TransactionDetail>()
        transactionList.forEach{
            arrayListTransaction.add(it)
        }
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTransactionJemaahList.layoutManager = layoutManager
        val adapter = TransactionPanitiaAdapter(arrayListTransaction)
        binding.rvTransactionJemaahList.adapter = adapter
    }

    override fun onResume() {
        transactionViewModel.getTransactionList(userPreferences.getUid()!!, true)
        super.onResume()
    }
    override fun onDestroyView() {
        transactionViewModel.apply{
            listTransactionDetail.removeObservers(viewLifecycleOwner)
            isLoading.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }
}
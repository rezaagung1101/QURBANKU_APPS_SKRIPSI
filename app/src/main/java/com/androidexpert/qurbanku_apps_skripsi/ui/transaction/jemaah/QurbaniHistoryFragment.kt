package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.adapter.transaction.QurbaniHistoryAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentQurbaniHistoryBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.TransactionViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class QurbaniHistoryFragment : Fragment() {
    private lateinit var binding: FragmentQurbaniHistoryBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val transactionRepository = TransactionRepository()
    private lateinit var userPreferences: UserPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQurbaniHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreference(requireContext())
        transactionViewModel = ViewModelProvider(this, ViewModelFactory.TransactionViewModelFactory(transactionRepository))[TransactionViewModel::class.java]
        getAcceptedTransaction(userPreferences.getUid()!!)
        binding.swipeRefresh.setOnRefreshListener {
            getAcceptedTransaction(userPreferences.getUid()!!)
            binding.swipeRefresh.isRefreshing = true
            // Use a Handler to post a delayed action
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
                binding.rvQurbaniHistory.smoothScrollToPosition(0)
            }, 1000)
        }
        transactionViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    fun getAcceptedTransaction(uid: String){
        transactionViewModel.getAcceptedTransaction(uid)
        transactionViewModel.listTransactionDetail.observe(viewLifecycleOwner){
            if(it!=null){
                setupInformation(it)
                binding.tvNullQurbani.alpha = 0f
            }else{
                binding.tvNullQurbani.alpha = 1f
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
        binding.rvQurbaniHistory.layoutManager = layoutManager
        val adapter = QurbaniHistoryAdapter(arrayListTransaction)
        binding.rvQurbaniHistory.adapter = adapter
    }

    override fun onDestroyView() {
        transactionViewModel.apply {
            listTransactionDetail.removeObservers(viewLifecycleOwner)
            isLoading.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }
}
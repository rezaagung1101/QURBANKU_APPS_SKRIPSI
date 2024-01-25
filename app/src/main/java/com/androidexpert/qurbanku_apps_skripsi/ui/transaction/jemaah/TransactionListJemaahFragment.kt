package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.adapter.transaction.TransactionJemaahAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentTransactionListJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.TransactionViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference


class TransactionListJemaahFragment : Fragment() {
    private lateinit var binding: FragmentTransactionListJemaahBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val transactionRepository = TransactionRepository()
    private lateinit var userPreferences: UserPreference

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
        userPreferences = UserPreference(requireContext())
        transactionViewModel = ViewModelProvider(this, ViewModelFactory.TransactionViewModelFactory(transactionRepository))[TransactionViewModel::class.java]
        getTransactionList(userPreferences.getUid()!!)
        binding.swipeRefresh.setOnRefreshListener {
            getTransactionList(userPreferences.getUid()!!)
            binding.swipeRefresh.isRefreshing = true
            // Use a Handler to post a delayed action
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
                binding.rvTransactionList.smoothScrollToPosition(0)
            }, 1000)
        }
    }

    fun getTransactionList(uid: String){
        transactionViewModel.getTransactionList(uid, false)
        transactionViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        transactionViewModel.listTransactionDetail.observe(viewLifecycleOwner){
            if(it!=null){
                setupInformation(it)
                binding.tvNullTransaction.alpha = 0f
            } else {
                binding.tvNullTransaction.alpha = 1f
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
        binding.rvTransactionList.layoutManager = layoutManager
        val adapter = TransactionJemaahAdapter(arrayListTransaction)
        binding.rvTransactionList.adapter = adapter
    }

    override fun onDestroyView() {
        transactionViewModel.apply{
            listTransactionDetail.removeObservers(viewLifecycleOwner)
            isLoading.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }

}
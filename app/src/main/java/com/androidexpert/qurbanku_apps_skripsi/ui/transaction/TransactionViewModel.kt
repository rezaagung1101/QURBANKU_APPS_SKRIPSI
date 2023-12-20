package com.androidexpert.qurbanku_apps_skripsi.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Transaction
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.utils.SingleLiveEvent
import java.io.File

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var _addTransactionResult = SingleLiveEvent<Boolean>()
    val addTransactionResult: LiveData<Boolean> = _addTransactionResult
    private var _transactionDetail = MutableLiveData<TransactionDetail>()
    val transactionDetail: LiveData<TransactionDetail> = _transactionDetail
    private var _listTransactionDetail = MutableLiveData<List<TransactionDetail>>()
    val listTransactionDetail: LiveData<List<TransactionDetail>> = _listTransactionDetail
    private var _confirmTransactionResult = SingleLiveEvent<Boolean>()
    val confirmTransactionResult: LiveData<Boolean> = _confirmTransactionResult

    fun addTransaction(
        transaction: Transaction,
        photoFile: File,
    ) {
        _isLoading.value = true
        transactionRepository.addTransaction(transaction, photoFile) { isSuccess, transactionData ->
            _addTransactionResult.value = isSuccess
            _transactionDetail.value = transactionData
            _isLoading.value = false
        }
    }

    fun getTransactionList(uid: String, isAdmin: Boolean) {
        _isLoading.value = true
        transactionRepository.getTransactionList(uid, isAdmin) { transactionData ->
            _listTransactionDetail.value = transactionData
            _isLoading.value = false
        }
    }

    fun getAcceptedTransaction(uid: String) {
        _isLoading.value = true
        transactionRepository.getAcceptedTransaction(uid) { transactionData ->
            _listTransactionDetail.value = transactionData
            _isLoading.value = false
        }
    }

    fun confirmTransaction(idJemaah: String, idAnimal: String, idTransaction: String, status: Boolean,note: String?) {
        _isLoading.value = true
        transactionRepository.confirmTransaction(
            idJemaah, idAnimal, idTransaction, status, note
        ) { isSuccess, updatedTransaction ->
            _confirmTransactionResult.value = isSuccess
            _transactionDetail.value = updatedTransaction
            _isLoading.value = false
        }
    }
}
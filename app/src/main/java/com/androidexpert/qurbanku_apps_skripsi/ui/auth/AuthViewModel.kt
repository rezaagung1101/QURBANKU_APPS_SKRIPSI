package com.androidexpert.qurbanku_apps_skripsi.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.data.remote.lib.user.User

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val isUsingLocation = MutableLiveData(false)
    val latitude = MutableLiveData(0.0)
    val longitude = MutableLiveData(0.0)
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> get() = _registrationResult

    fun signUpUser(user: User, password: String) {
        _isLoading.value = true
        authRepository.signUpUser(user, password) { isSuccess ->
            _registrationResult.value = isSuccess
            _isLoading.value = false
        }
    }


}
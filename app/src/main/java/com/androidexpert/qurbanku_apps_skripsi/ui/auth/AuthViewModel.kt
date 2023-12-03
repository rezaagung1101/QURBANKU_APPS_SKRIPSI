package com.androidexpert.qurbanku_apps_skripsi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.utils.SingleLiveEvent

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registrationResult = SingleLiveEvent<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult
    private var _loginResult = SingleLiveEvent<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val isUsingLocation = MutableLiveData(false)
    val latitude = MutableLiveData(0.0)
    val longitude = MutableLiveData(0.0)

    fun signUpUser(user: User, password: String) {
        _isLoading.value = true
        authRepository.signUpUser(user, password) { isSuccess ->
            _registrationResult.value = isSuccess
            _isLoading.value = false
        }
    }
    fun login(email: String, password: String) {
        _isLoading.value = true
        authRepository.login(email, password) { isSuccess, userData->
            _loginResult.value = isSuccess
            _user.value = userData
            _isLoading.value = false
        }
    }


}
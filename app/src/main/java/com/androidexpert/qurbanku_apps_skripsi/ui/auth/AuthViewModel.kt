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
    private val _isUsingLocation = MutableLiveData(false)
    val isUsingLocation:LiveData<Boolean> = _isUsingLocation
    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude
    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude

    fun signUpUser(user: User, password: String) {
        _isLoading.value = true
        authRepository.signUpUser(user, password) { isSuccess ->
            _registrationResult.value = isSuccess
            _isLoading.value = false
            _registrationResult.callHandled()
        }
    }
    fun login(email: String, password: String) {
        _isLoading.value = true
        authRepository.login(email, password) { isSuccess, userData->
            _loginResult.value = isSuccess
            _user.value = userData
            _isLoading.value = false
            _loginResult.callHandled()
        }
    }

    fun setLocation(isUsingLocation: Boolean, latitude: Double, longitude: Double){
        _isUsingLocation.value = isUsingLocation
        _latitude.value = latitude
        _longitude.value = longitude
    }


}
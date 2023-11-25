package com.androidexpert.qurbanku_apps_skripsi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private var _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val isUsingLocation = MutableLiveData(false)
    val latitude = MutableLiveData(0.0)
    val longitude = MutableLiveData(0.0)

}
package com.androidexpert.qurbanku_apps_skripsi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AuthRepository
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.AuthViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel

object ViewModelFactory {
    class AuthViewModelFactory(private val authRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    class UserViewModelFactory(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

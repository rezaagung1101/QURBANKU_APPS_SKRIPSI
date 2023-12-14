package com.androidexpert.qurbanku_apps_skripsi.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.utils.SingleLiveEvent
import com.google.android.gms.maps.model.LatLng

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private var _updateResult = SingleLiveEvent<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult
    private var _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser
    private var _listMasjidUser = MutableLiveData<List<MasjidUser>>()
    val listMasjidUser: LiveData<List<MasjidUser>> = _listMasjidUser
    private var _qurbaniAmount = MutableLiveData<Int>()
    val qurbaniAmount: LiveData<Int> = _qurbaniAmount
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val coordinateLocation = MutableLiveData(LatLng(-2.548926, 118.0148634))
    val isUsingLocation = MutableLiveData(false)
    val latitude = MutableLiveData(0.0)
    val longitude = MutableLiveData(0.0)

    fun logout() {
        userRepository.logout()
    }

    fun getProfile(uid: String) {
        _isLoading.value = true
        userRepository.getProfile(uid) { user ->
            _user.value = user
            _isLoading.value = false
        }
    }

    fun getMasjidList() {
        _isLoading.value = true
        userRepository.getMasjidList { masjidUserList ->
            _listMasjidUser.value = masjidUserList
            _isLoading.value = false
        }
    }

    fun getJemaahList(idJemaahList: List<String>) {
        _isLoading.value = true
        userRepository.getJemaahList(idJemaahList) { listUser ->
            _listUser.value = listUser
            _isLoading.value = false
        }
    }

    fun updatePanitiaProfile(
        uid: String,
        name: String,
        headName: String,
        phoneNumber: String,
        latitude: Double,
        longitude: Double,
        accountNumber: String,
        bankName: String,
        accountName: String,
    ) {
        _isLoading.value = true
        userRepository.updatePanitiaProfile(uid, name, headName, phoneNumber, latitude, longitude, accountNumber, bankName, accountName){ isSuccess, userData ->
            _isLoading.value = false
            _updateResult.value = isSuccess
            _user.value = userData
        }
    }

    fun updateJemaahProfile(
        uid: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
    ) {
        _isLoading.value = true
        userRepository.updateJemaahProfile(uid, phoneNumber, name, address, headName){ isSuccess, userData ->
            _isLoading.value = false
            _updateResult.value = isSuccess
            _user.value = userData
        }
    }

    fun getQurbaniAmount(uid: String){
        _isLoading.value = true
        userRepository.getQurbaniAmount(uid){ qurbaniAmount ->
            _qurbaniAmount.value = qurbaniAmount
            _isLoading.value = false
        }
    }

}
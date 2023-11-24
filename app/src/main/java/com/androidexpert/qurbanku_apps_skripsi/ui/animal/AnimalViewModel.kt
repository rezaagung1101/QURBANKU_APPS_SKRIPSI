package com.androidexpert.qurbanku_apps_skripsi.ui.animal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnimalViewModel : ViewModel() {
    private var _selectedDate = MutableLiveData<Long>()
    val selectedDate: LiveData<Long> = _selectedDate

    fun setDate(date: Long) {
        _selectedDate.value = date
    }
}
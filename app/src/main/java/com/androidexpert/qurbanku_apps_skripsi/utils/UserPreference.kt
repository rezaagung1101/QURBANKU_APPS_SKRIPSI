package com.androidexpert.qurbanku_apps_skripsi.utils

import android.content.Context
import android.content.SharedPreferences
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User

class UserPreference(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("setting", Context.MODE_PRIVATE)
    private val preferences: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveAvailableAnimalAmount(availableAnimal: Int) {
        preferences.putString(Constanta.available_animal, availableAnimal.toString())
        preferences.apply()
    }

    fun saveJemaahPreference(user: User) {
        preferences.putBoolean(Constanta.isLogin, true)
        preferences.putString(Constanta.uid, user.uid)
        preferences.putString(Constanta.email, user.email)
        preferences.putString(Constanta.name, user.name)
        preferences.putString(Constanta.headName, user.headName)
        preferences.putString(Constanta.phoneNumber, user.phoneNumber)
        preferences.putBoolean(Constanta.isAdmin, user.admin ?: false)
        preferences.putString(Constanta.address, user.address)
        preferences.apply()
    }

    fun savePanitiaPreference(user: User) {
        preferences.putBoolean(Constanta.isLogin, true)
        preferences.putString(Constanta.uid, user.uid)
        preferences.putString(Constanta.email, user.email)
        preferences.putString(Constanta.name, user.name)
        preferences.putString(Constanta.headName, user.headName)
        preferences.putString(Constanta.phoneNumber, user.phoneNumber)
        preferences.putBoolean(Constanta.isAdmin, user.admin)
        preferences.putString(Constanta.latitude, user.latitude.toString())
        preferences.putString(Constanta.longitude, user.longitude.toString())
        preferences.putString(Constanta.bankName, user.bankName)
        preferences.putString(Constanta.bankAccountNumber, user.bankAccountNumber)
        preferences.putString(Constanta.bankAccountName, user.bankAccountName)
        preferences.apply()
    }


    fun logout() {
        preferences.clear().apply()
    }

    fun getUid(): String? {
        return sharedPreferences.getString(Constanta.uid, null)
    }

    fun getAvailableAnimal(): String?{
        return sharedPreferences.getString(Constanta.available_animal, null)
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(Constanta.isLogin, false)
    }

    fun isAdmin(): Boolean {
        return sharedPreferences.getBoolean(Constanta.isAdmin, false)
    }

    fun getName(): String? {
        return sharedPreferences.getString(Constanta.name, null)
    }

    fun getJemaahData(): User {
        return User(
            uid = sharedPreferences.getString(Constanta.uid, null)!!,
            email = sharedPreferences.getString(Constanta.email, null)!!,
            name = sharedPreferences.getString(Constanta.name, null)!!,
            headName = sharedPreferences.getString(Constanta.headName, null)!!,
            phoneNumber = sharedPreferences.getString(Constanta.phoneNumber, null)!!,
            admin = false,
            address = sharedPreferences.getString(Constanta.address, null)!!,
            latitude = null,
            longitude = null,
            bankName = null,
            bankAccountNumber = null,
            bankAccountName = null
        )
    }
    fun getPanitiaData(): User{
        return User(
            uid = sharedPreferences.getString(Constanta.uid, null)!!,
            email = sharedPreferences.getString(Constanta.email, null)!!,
            name = sharedPreferences.getString(Constanta.name, null)!!,
            headName = sharedPreferences.getString(Constanta.headName, null)!!,
            phoneNumber = sharedPreferences.getString(Constanta.phoneNumber, null)!!,
            admin = false,
            address = null,
            latitude = sharedPreferences.getString(Constanta.latitude, null)!!.toDouble(),
            longitude = sharedPreferences.getString(Constanta.longitude, null)!!.toDouble(),
            bankName = sharedPreferences.getString(Constanta.bankName, null)!!,
            bankAccountNumber = sharedPreferences.getString(Constanta.bankAccountNumber, null)!!,
            bankAccountName = sharedPreferences.getString(Constanta.bankAccountName, null)!!
        )
    }


}
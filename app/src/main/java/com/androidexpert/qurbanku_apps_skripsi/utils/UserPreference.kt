package com.androidexpert.qurbanku_apps_skripsi.utils

import android.content.Context
import android.content.SharedPreferences
import com.androidexpert.qurbanku_apps_skripsi.data.remote.lib.user.User

class UserPreference(context : Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
    private val preferences : SharedPreferences.Editor = sharedPreferences.edit()

    fun getName() : String? {
        return sharedPreferences.getString("NAME",null)
    }
    fun saveUser(user: User){
        preferences.putBoolean("LOGIN",true)
        preferences.putString("NAME", user.name)
        preferences.putString("UID", user.uid)
        preferences.apply()
    }

    fun logout(){
        preferences.clear().apply()
    }
    fun getUid(): String? {
        return sharedPreferences.getString("UID",null)
    }
    fun isLogin() : Boolean{
        return sharedPreferences.getBoolean("LOGIN",false)
    }
}
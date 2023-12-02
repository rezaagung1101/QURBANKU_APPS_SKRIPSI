package com.androidexpert.qurbanku_apps_skripsi.data.remote.lib.user

data class User(
    val uid: String,
    val email: String,
    val name: String,
    val headName: String,
    val phoneNumber: String,
    val isAdmin: Boolean,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val bankName: String?,
    val bankAccountNumber: String?,
    val bankAccountName: String?
)
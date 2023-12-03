package com.androidexpert.qurbanku_apps_skripsi.data.lib

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User @JvmOverloads constructor(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val headName: String = "",
    val phoneNumber: String = "",
    val admin: Boolean = false,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val bankName: String? = null,
    val bankAccountNumber: String? = null,
    val bankAccountName: String? = null
): Parcelable
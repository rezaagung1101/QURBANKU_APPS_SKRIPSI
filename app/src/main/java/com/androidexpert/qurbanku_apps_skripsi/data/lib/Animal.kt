package com.androidexpert.qurbanku_apps_skripsi.data.lib

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal @JvmOverloads constructor(
    val id: String = "",
    val photoUrl: String = "",
    val qurbaniTimeMillisecond: Long = 0,
    val note: String? = null,
    val speciesName: String = "",
    val varietyName: String = "",
    val weight: Double = 0.0,
    val color: String = "",
    val status: Boolean = false,
    val operationalCosts: Int = 0,
    val price: Int = 0,
    val jointVentureAmount: Int = 0,
    val idMasjid: String = "", //user's ID
    val idShohibulQurbaniList: List<String>? = null
): Parcelable
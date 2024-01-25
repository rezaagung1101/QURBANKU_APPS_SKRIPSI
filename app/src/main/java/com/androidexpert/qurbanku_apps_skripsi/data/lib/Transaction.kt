package com.androidexpert.qurbanku_apps_skripsi.data.lib

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction @JvmOverloads constructor(
    val id: String = "",
    val photoUrl: String = "",
    val createdTimeMillisecond: Long = 0,
    val status: Boolean? = null,
    val note: String? = null,
    val idJemaah: String = "",
    val idMasjid: String = "",
    val idAnimal: String = ""
): Parcelable

@Parcelize
data class TransactionDetail @JvmOverloads constructor(
    val transaction: Transaction? = null,
    val jemaah: User? = null,
    val masjid: User? = null,
    val animal: Animal? = null
): Parcelable
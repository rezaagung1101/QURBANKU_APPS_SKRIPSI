package com.androidexpert.qurbanku_apps_skripsi.utils

import android.Manifest

object Constanta {
    val DATE_DATA = "DATE"

    //login
    const val isPanitia = "IS_PANITIA"
    const val passwordMinimum = 6

    //maps
    const val latitude = "LATITUDE"
    const val longitude = "LONGITUDE"
    const val usingLocation = "USINGLOCATION"

    //camera
    const val CAMERA_X_RESULT = 200
    const val picture = "PICTURE"
    const val isBackCamera = "IS_BACK_CAMERA"
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val REQUEST_CODE_PERMISSIONS = 10

    //preference
    const val isLogin = "LOGIN"
    const val email = "EMAIL"
    const val uid = "UID"
    const val name = "NAME"
    const val headName = "HEAD_NAME"
    const val phoneNumber = "PHONE_NUMBER"
    const val isAdmin = "IS_ADMIN"
    const val address = "ADDRESS"
    const val bankName = "BANK_NAME"
    const val bankAccountNumber = "BANK_ACCOUNT_NUMBER"
    const val bankAccountName = "BANK_ACCOUNT_NAME"

    //data
    const val USER_DATA = "USER_DATA"
    const val ANIMAL_DATA = "ANIMAL_DATA"
    const val TRANSACTION_DATA = "TRANSACTION_DATA"

}
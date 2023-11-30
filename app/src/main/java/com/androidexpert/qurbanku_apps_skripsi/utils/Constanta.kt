package com.androidexpert.qurbanku_apps_skripsi.utils

import android.Manifest

object Constanta {
    val DATE_DATA = "DATE"
    //login
    const val isPanitia= "IS_PANITIA"
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
}
package com.androidexpert.qurbanku_apps_skripsi.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Helper {


}
fun convertMillisToString(timeMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeMillis
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(calendar.time)
}

enum class ACTOR {
    PANITIA, JEMAAH
}
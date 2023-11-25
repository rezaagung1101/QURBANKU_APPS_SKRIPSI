package com.androidexpert.qurbanku_apps_skripsi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Helper {
    private const val FILENAME_FORMAT = "dd-MMMM-yyyy"
    private const val simpleDateFormat = "dd MMM yyyy HH.mm"
    private const val timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    fun getCurrentDate(): Date {
        return Date()
    }
    fun formatLongDateToString(longDate: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(longDate)
        return dateFormat.format(date)
    }

    @SuppressLint("ConstantLocale")
    val simpleDate = SimpleDateFormat(simpleDateFormat, Locale.getDefault())
    fun getSimpleDate(date: Date): String = simpleDate.format(date)
    fun getLongSimpleDate(timeMillis: Long): String = formatLongDateToString(timeMillis, FILENAME_FORMAT)
    fun getLongSimpleDateTransaction(timeMillis: Long): String = formatLongDateToString(timeMillis, simpleDateFormat)
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }

        fun parseAddress(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address: String = if (addresses?.isNotEmpty() == true) {
            val fetchedAddress: Address = addresses[0]
            // Extract the address details you need, e.g.
//            fetchedAddress.getAddressLine(0)
            "${fetchedAddress.subLocality}, ${fetchedAddress.locality}"
        } else {
            "Address not found"
        }
        return address
    }
//    fun parseAddress(context: Context, latitude: Double, longitude: Double): String {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//        val address: String = if (addresses?.isNotEmpty() == true) {
//            val fetchedAddress: Address = addresses[0]
//            // Extract the address details you need, e.g.
//            fetchedAddress.getAddressLine(0)
////            val cityTemp = "${fetchedAddress.subAdminArea}"
//            val cityTemp = fetchedAddress.getAddressLine(3)
////            val city = cityTemp.split("\\s".toRegex()).toTypedArray()
////            return city[1]
//            return cityTemp
//        } else {
//            "Address not found"
//        }
//        return address
//    }
}

object DialogUtils {
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonListener: () -> Unit,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.next2)) { _, _ ->
            positiveButtonListener.invoke()
        }
        builder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
            // Handle cancel if needed
        }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.danger))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.green_main))
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
    }
}

object Constanta{
    const val latitude = "LATITUDE"
    const val longitude = "LONGITUDE"
    const val usingLocation = "USINGLOCATION"
}
enum class ACTOR {
    PANITIA, JEMAAH
}
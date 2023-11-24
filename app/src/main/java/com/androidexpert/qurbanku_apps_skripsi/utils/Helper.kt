package com.androidexpert.qurbanku_apps_skripsi.utils

import android.annotation.SuppressLint
import android.content.Context
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


enum class ACTOR {
    PANITIA, JEMAAH
}
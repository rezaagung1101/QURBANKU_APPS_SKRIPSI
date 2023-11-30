package com.androidexpert.qurbanku_apps_skripsi.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R


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
    fun showNotificationDialog(
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
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.green_main))
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
    }
}

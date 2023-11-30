package com.androidexpert.qurbanku_apps_skripsi.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Helper {
    private const val FILENAME_FORMAT = "dd-MMMM-yyyy"
    private const val simpleDateFormat = "dd MMM yyyy HH.mm"
    private const val timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    //for camera
    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

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
    fun getLongSimpleDate(timeMillis: Long): String =
        formatLongDateToString(timeMillis, FILENAME_FORMAT)

    fun getLongSimpleDateTransaction(timeMillis: Long): String =
        formatLongDateToString(timeMillis, simpleDateFormat)

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

    fun parseAddressCity(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address: String = if (addresses?.isNotEmpty() == true) {
            val fetchedAddress: Address = addresses[0]
            // Extract the address details you need, e.g.
            fetchedAddress.getAddressLine(0)
            val cityTemp = "${fetchedAddress.subAdminArea}"
//            val cityTemp = fetchedAddress.getAddressLine(3)
            val city = cityTemp.split("\\s".toRegex()).toTypedArray()
            return city[1]
        } else {
            "Address not found"
        }
        return address
    }

    fun rotateFile(file: File, isBackCamera: Boolean = false) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
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

object Constanta {
    val DATE_DATA = "DATE"
    const val isPanitia= "IS_PANITIA"

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

enum class ACTOR {
    PANITIA, JEMAAH
}
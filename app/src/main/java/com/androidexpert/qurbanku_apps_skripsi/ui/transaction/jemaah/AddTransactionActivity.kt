package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddTransactionBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.CameraActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import java.io.File

class AddTransactionActivity : AppCompatActivity() {
    /**
     * Terima data dari Detail Hewan milik jemaah
     */
    private lateinit var binding: ActivityAddTransactionBinding
    private var photoUrl: String = ""
    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Constanta.CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra(Constanta.picture, File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra(Constanta.picture)
            } as? File

            val isBackCamera = it.data?.getBooleanExtra(Constanta.isBackCamera, true) as Boolean

            myFile?.let { file ->
                Helper.rotateFile(file, isBackCamera)
                getFile = file
                binding.ivTransactionPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.ivTransactionPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                setupInformation(true)
            }
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = Helper.uriToFile(uri, this@AddTransactionActivity)
                getFile = myFile
                binding.ivTransactionPhoto.setImageURI(uri)
                binding.ivTransactionPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                setupInformation(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.transfer_requirements)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupInformation(false)
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
    }

    fun setupInformation(isPhotoSelected: Boolean) {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                Constanta.REQUIRED_PERMISSIONS,
                Constanta.REQUEST_CODE_PERMISSIONS
            )
        }
        if (isPhotoSelected == true) {
            binding.btnSend.setBackgroundColor(resources.getColor(R.color.green_main))
            binding.btnSend.setTextColor(resources.getColor(R.color.white))
            binding.btnSend.setOnClickListener {
                val title = resources.getString(R.string.send_transaction_proof)
                val message = resources.getString(R.string.send_transaction_message)
                DialogUtils.showConfirmationDialog(this, title, message, ::addTransaction)
            }
        } else {
            binding.btnSend.setOnClickListener {
                Toast.makeText(
                    this,
                    resources.getString(R.string.photo_not_selected),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.btnSend.setBackgroundColor(resources.getColor(R.color.disabled_background))
            binding.btnSend.setTextColor(resources.getColor(R.color.disabled_text))
        }
        val stringArray = resources.getStringArray(R.array.transaction_photo_requirement_value)
        val text = stringArray.joinToString("\n")
        binding.tvPhotoRequirementValue.text = text
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun allPermissionsGranted() = Constanta.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constanta.REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_not_permitted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    fun addTransaction() {
        /**
         * kirim data ke intent
         */
        val intent = Intent(this, DetailTransactionJemaahActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
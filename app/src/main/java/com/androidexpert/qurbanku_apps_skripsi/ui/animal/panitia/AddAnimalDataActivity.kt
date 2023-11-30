package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddAnimalDataBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.CameraActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import java.io.File

class AddAnimalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAnimalDataBinding
    private var jointVentureValue: Int = 1
    private var animalSpecies: String = ""
    private var dueDateMillis: Long = System.currentTimeMillis()
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
                binding.ivAnimal.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.ivAnimal.scaleType = ScaleType.CENTER_CROP
            }
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = Helper.uriToFile(uri, this@AddAnimalDataActivity)
                getFile = myFile
                binding.ivAnimal.setImageURI(uri)
                binding.ivAnimal.scaleType = ScaleType.CENTER_CROP
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAnimalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dueDateMillis = intent.getLongExtra(Constanta.DATE_DATA, System.currentTimeMillis())
        Toast.makeText(
            this,
            Helper.convertMillisToString(dueDateMillis),
            Toast.LENGTH_LONG
        ).show()
        setupInformation()
        setupSpinner()
        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture))
    }

    private fun setupInformation() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                Constanta.REQUIRED_PERMISSIONS,
                Constanta.REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnSave.setOnClickListener {
            val title = resources.getString(R.string.save_data_animal)
            val message = resources.getString(R.string.save_data_animal_message)
            DialogUtils.showConfirmationDialog(this, title, message, ::addAnimal)
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
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

    private fun addAnimal() {
        //set the algorithm for viewmodel
        startActivity(Intent(this, DetailPanitiaAnimalActivity::class.java))
    }

    private fun setupSpinner() {
        val animalList = resources.getStringArray(R.array.spinner_qurban_animal)
        val animalAdapter =
            ArrayAdapter(this, R.layout.spinner_item, animalList)
        animalAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.btnSpinnerAnimal.adapter = animalAdapter
        binding.btnSpinnerAnimal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    animalSpecies = animalList[position].toString()
                    if (position == 1 || position == 2) {
                        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture_disabled))
                    } else {
                        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture))
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    animalSpecies = animalList[0].toString()
                }
            }
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

    private fun allPermissionsGranted() = Constanta.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupSpinnerJointVenture(jointVentureList: Array<String>) {
        val array = jointVentureList
        val jointVentureAdapter =
            ArrayAdapter(this, R.layout.spinner_item, array)
        jointVentureAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.btnSpinnerJointVenture.adapter = jointVentureAdapter
        binding.btnSpinnerJointVenture.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    jointVentureValue = array[position].toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    jointVentureValue = array[0].toInt()
                }
            }
    }

}
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
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddAnimalDataBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.CameraActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference
import java.io.File

class AddAnimalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAnimalDataBinding
    private var jointVentureValue: Int = 1
    private var animalSpecies: String = ""
    private var qurbaniTimeMillisecond: Long = System.currentTimeMillis()
    private var getFile: File? = null
    private var isPhotoSelected = false
    private val animalRepository = AnimalRepository()
    private lateinit var animalViewModel: AnimalViewModel
    private lateinit var userPreference: UserPreference
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

            //val isBackCamera = it.data?.getBooleanExtra(Constanta.isBackCamera, true) as Boolean

            myFile?.let { file ->
                //Helper.rotateFile(file, isBackCamera)
                getFile = file
                binding.ivAnimal.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.ivAnimal.scaleType = ScaleType.CENTER_CROP
                isPhotoSelected = true
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
                isPhotoSelected = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAnimalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animalViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AnimalViewModelFactory(animalRepository)
        )[AnimalViewModel::class.java]
        userPreference = UserPreference(this)
        qurbaniTimeMillisecond =
            intent.getLongExtra(Constanta.DATE_DATA, System.currentTimeMillis())
        setupInformation()
        setupSpinner()
        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture))
    }

    private fun setupInformation() {
        animalViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                Constanta.REQUIRED_PERMISSIONS,
                Constanta.REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnSave.setOnClickListener {
            binding.apply {
                val title = resources.getString(R.string.save_data_animal)
                val message = resources.getString(R.string.save_data_animal_message)
                val variety = etVariety.text.toString()
                val weight =
                    if (etWeight.text.isNotEmpty()) etWeight.text.toString()
                    else "0.0"
                val color = etColor.text.toString()
                val operationalCosts =
                    if (etOperationalCost.text.isNotEmpty()) etOperationalCost.text.toString()
                    else "0"
                val price =
                    if (etPrice.text.isNotEmpty()) etPrice.text.toString()
                    else "0"
                val note = etNote.text.toString()
                val animal = Animal(
                    id = "",
                    photoUrl = "",
                    qurbaniTimeMillisecond = qurbaniTimeMillisecond,
                    note = note,
                    speciesName = animalSpecies,
                    varietyName = variety,
                    weight = weight.toDouble(),
                    color = color,
                    status = false,
                    operationalCosts = operationalCosts.toInt(),
                    price = price.toInt(),
                    jointVentureAmount = jointVentureValue,
                    idMasjid = userPreference.getUid()!!,
                    idShohibulQurbaniList = null
                )
                if (validation(animal, isPhotoSelected)) {
                    DialogUtils.showConfirmationDialog(this@AddAnimalDataActivity, title, message) {
                        addAnimal(animal, getFile!!)
                    }
                }
            }
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
    }

    fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    fun validation(animal: Animal, isPhotoSelected: Boolean): Boolean {
        var isValid = false
        binding.apply {
            val isVarietyValid = animal.varietyName.isNotEmpty()
            val isWeightValid = animal.weight.toString() != "0.0"
            val isColorValid = animal.color.isNotEmpty()
            val isOperationalCostsValid = animal.operationalCosts.toString() != "0"
            val isPriceValid = animal.price.toString() != "0"
            val editTextPairs = listOf(
                etVariety to etVarietyLayout,
                etWeight to etWeightLayout,
                etColor to etColorLayout,
                etOperationalCost to etOperationalCostLayout,
                etPrice to etPriceLayout,
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isVarietyValid && isWeightValid && isColorValid && isOperationalCostsValid && isPriceValid && isPhotoSelected) {
                isValid = true
            }

            val validationList = listOf(
                isVarietyValid to null,
                isWeightValid to null,
                isColorValid to null,
                isOperationalCostsValid to null,
                isPriceValid to null
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid!!) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(this@AddAnimalDataActivity, editText, layout, errorMessage)
                    }
                }
            }

            if (!isPhotoSelected) Toast.makeText(
                this@AddAnimalDataActivity,
                resources.getString(R.string.animal_photo_not_selected),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }

    fun addAnimal(animal: Animal, photo: File) {
        animalViewModel.addAnimal(animal, Helper.reduceFileImage(photo))
        animalViewModel.addAnimalStatusResult.observe(this, { isSuccess ->
            if (isSuccess) {
                animalViewModel.animal.observe(this, { animal ->
                    showDialog(isSuccess, animal)
                })
            } else {
                showDialog(false, null)
            }
        })
    }

    fun showDialog(status: Boolean, animal: Animal?){
        val messageResId = if (status) R.string.add_animal_success else R.string.add_animal_failed
        val title = resources.getString(R.string.announcement)
        val message = resources.getString(messageResId)

        DialogUtils.showNotificationDialog(this, title, message) {
            if(status){
                val intent = Intent(this, DetailPanitiaAnimalActivity::class.java)
                intent.putExtra(Constanta.ANIMAL_DATA, animal!!)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setupSpinner() {
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

    fun allPermissionsGranted() = Constanta.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun setupSpinnerJointVenture(jointVentureList: Array<String>) {
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

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}
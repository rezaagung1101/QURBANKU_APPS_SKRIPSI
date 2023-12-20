package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Transaction
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddTransactionBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.CameraActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.TransactionViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference
import java.io.File

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val transactionRepository = TransactionRepository()
    private lateinit var userPreference: UserPreference
    private var animalData = Animal()
    private var masjidData = User()
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
        animalData = intent.getParcelableExtra<Animal>(Constanta.ANIMAL_DATA) as Animal
        masjidData = intent.getParcelableExtra<User>(Constanta.USER_DATA) as User
        userPreference = UserPreference(this)
        transactionViewModel = ViewModelProvider(
            this,
            ViewModelFactory.TransactionViewModelFactory(transactionRepository)
        )[TransactionViewModel::class.java]
        setupInformation(false)
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        transactionViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    fun setupInformation(isPhotoSelected: Boolean) {
        binding.apply {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this@AddTransactionActivity,
                    Constanta.REQUIRED_PERMISSIONS,
                    Constanta.REQUEST_CODE_PERMISSIONS
                )
            }
            if (isPhotoSelected == true) {
                btnSend.setBackgroundColor(resources.getColor(R.color.green_main))
                btnSend.setTextColor(resources.getColor(R.color.white))
                btnSend.setOnClickListener {
                    val title = resources.getString(R.string.send_transaction_proof)
                    val message = resources.getString(R.string.send_transaction_message)
                    DialogUtils.showConfirmationDialog(this@AddTransactionActivity, title, message){
                        addTransaction(Helper.reduceFileImage(getFile!!))
                    }
                }
            } else {
                btnSend.setOnClickListener {
                    Toast.makeText(
                        this@AddTransactionActivity,
                        resources.getString(R.string.photo_not_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                btnSend.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnSend.setTextColor(resources.getColor(R.color.disabled_text))
            }
            val stringArray = resources.getStringArray(R.array.transaction_photo_requirement_value)
            val text = stringArray.joinToString("\n")
            val totalPrice = animalData.price + animalData.operationalCosts
            val costRequired = totalPrice / animalData.jointVentureAmount
            tvPhotoRequirementValue.text = text
            tvBankName.text = masjidData.bankName
            tvAccountName.text = masjidData.bankAccountName
            tvAccountNumber.text = masjidData.bankAccountNumber
            tvTransferNominal.text = resources.getString(
                R.string.price_2,
                Helper.parseNumberFormat(costRequired)
            )
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

    fun addTransaction(photo: File) {
        val transaction = Transaction(
            id = "",
            photoUrl = "",
            createdTimeMillisecond = System.currentTimeMillis(),
            status = null,
            note = null,
            idJemaah = userPreference.getUid()!!,
            idMasjid = masjidData.uid,
            idAnimal = animalData.id
        )
        transactionViewModel.addTransaction(transaction, photo)
        transactionViewModel.addTransactionResult.observe(this) { isSuccess ->
            if (isSuccess) {
                transactionViewModel.transactionDetail.observe(this) { transaction ->
                    showDialog(isSuccess, transaction)
                }
            } else {
                showDialog(false, null)
            }
        }
    }

    fun showDialog(status: Boolean, transaction: TransactionDetail?) {
        val messageResId =
            if (status) R.string.add_transaction_success else R.string.add_transaction_failed
        val title = resources.getString(R.string.announcement)
        val message = resources.getString(messageResId)

        DialogUtils.showNotificationDialog(this, title, message) {
            if (status) {
                val intent = Intent(this, DetailTransactionJemaahActivity::class.java)
                intent.putExtra(Constanta.TRANSACTION_DATA, transaction!!)
                startActivity(intent)
                finish()
            }
        }
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsUpdateLocationActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

/**
 * Kayanya perlu bikin satu activity update maps deh
 */
class UpdateProfilePanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfilePanitiaBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var usingLocation: Boolean? = false
    private val launcerIntentLocation = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { result ->
                usingLocation = result.getBooleanExtra(Constanta.usingLocation, false)
                userViewModel.apply {
                    isUsingLocation.postValue(usingLocation)
                    val tempLatitude = result.getDoubleExtra(Constanta.latitude, 0.0)
                    val tempLongitude = result.getDoubleExtra(Constanta.longitude, 0.0)
                    latitude.postValue(tempLatitude)
                    longitude.postValue(tempLongitude)
                    setAddress(tempLatitude, tempLongitude)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfilePanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel =
            ViewModelProvider(
                this, ViewModelFactory.UserViewModelFactory(userRepository)
            )[UserViewModel::class.java]
        userViewModel.isLoading.observe(this@UpdateProfilePanitiaActivity) {
            showLoading(it)
        }
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userPreference = UserPreference(this)
        val user = userPreference.getPanitiaData()
        setupInformation(user)
        binding.btnAddAddress.setOnClickListener {
            pickLocation(user.latitude!!, user.longitude!!)
        }
        binding.btnSaveUpdate.setOnClickListener {
            binding.apply {
                val uid = userPreference.getUid()!!
                val name = etName.text.toString()
                val headName = etNameHeadTakmir.text.toString()
                val phoneNumber = etContactPersonNumber.text.toString()
                val accountNumber = etAccountNumber.text.toString()
                val bankName = etBankName.text.toString()
                val accountName = etAccountName.text.toString()
                //if success
                if (validation(
                        name,
                        headName,
                        phoneNumber,
                        accountNumber,
                        bankName,
                        accountName
                    )
                ) {
                    val title = resources.getString(R.string.update_profile)
                    val message = resources.getString(R.string.update_profile_message)
                    DialogUtils.showConfirmationDialog(
                        this@UpdateProfilePanitiaActivity,
                        title,
                        message,
                        {
                            updateProfile(
                                uid,
                                name,
                                headName,
                                phoneNumber,
                                latitude,
                                longitude,
                                accountNumber,
                                bankName,
                                accountName
                            )
                        })
                }
            }
        }
    }

    fun updateProfile(
        uid:String,
        name: String,
        headName: String,
        phoneNumber: String,
        latitude: Double,
        longitude: Double,
        accountNumber: String,
        bankName: String,
        accountName: String,
    ) {
        //call viewModel
        userViewModel.apply {
            updatePanitiaProfile(uid, name, headName, phoneNumber, latitude, longitude, accountNumber, bankName, accountName)
            updateResult.observe(this@UpdateProfilePanitiaActivity) { isSuccess ->
                if (isSuccess) {
                    user.observe(this@UpdateProfilePanitiaActivity){ updateduser ->
                        userPreference.savePanitiaPreference(updateduser)
                    }
                    val title = resources.getString(R.string.announcement)
                    val message = resources.getString(R.string.update_profile_success)
                    DialogUtils.showNotificationDialog(this@UpdateProfilePanitiaActivity, title, message, ::onBackPressed)
                } else{
                    val title = resources.getString(R.string.announcement)
                    val message = resources.getString(R.string.update_profile_failed)
                    DialogUtils.showNotificationDialog(this@UpdateProfilePanitiaActivity, title, message,{})
                }
            }
        }

    }

    fun pickLocation(latitude: Double, longitude: Double) {
        val intent = Intent(this, MapsUpdateLocationActivity::class.java)
        intent.putExtra(Constanta.latitude, latitude)
        intent.putExtra(Constanta.longitude, longitude)
        launcerIntentLocation.launch(intent)
    }

    fun validation(
        name: String,
        headName: String,
        phoneNumber: String,
        accountNumber: String,
        bankName: String,
        accountName: String,
    ): Boolean {
        var isValid = false
        binding.apply {
            val isNameValid = name.isNotEmpty()
            val isHeadNameValid = headName.isNotEmpty()
            val isPhoneNumberValid = phoneNumber.isNotEmpty()
            val isAccountNumberValid = accountNumber.isNotEmpty()
            val isBankNameValid = bankName.isNotEmpty()
            val isAccountNameValid = accountName.isNotEmpty()
            val editTextPairs = listOf(
                etName to etNameLayout,
                etNameHeadTakmir to etNameHeadTakmirLayout,
                etContactPersonNumber to etContactPersonNumberLayout,
                etAccountNumber to etAccountNumberLayout,
                etBankName to etBankNameLayout,
                etAccountName to etAccountNameLayout
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isNameValid && isHeadNameValid && isPhoneNumberValid && isAccountNumberValid && isBankNameValid && isAccountNameValid) {
                isValid = true
            }

            val validationList = listOf(
                isNameValid to null,
                isHeadNameValid to null,
                isPhoneNumberValid to null,
                isAccountNumberValid to null,
                isBankNameValid to null,
                isAccountNameValid to null
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(
                            this@UpdateProfilePanitiaActivity,
                            editText,
                            layout,
                            errorMessage
                        )
                    }
                }
            }

        }
        return isValid
    }

    fun setAddress(latitude: Double?, longitude: Double?) {
        if (latitude != null && longitude != null) {
            userViewModel.isUsingLocation.observe(this, { usingLocation ->
                if (usingLocation == true) binding.btnAddAddress.text =
                    Helper.parseAddress(this, latitude, longitude)
            })
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    fun setupInformation(user: User) {
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
        binding.apply {
            etName.setText(user.name)
            etContactPersonNumber.setText(user.phoneNumber)
            setAddress(user.latitude, user.longitude)
            btnAddAddress.text = Helper.parseAddress(
                this@UpdateProfilePanitiaActivity,
                user.latitude!!,
                user.longitude!!
            )
            etNameHeadTakmir.setText(user.headName)
            etAccountName.setText(user.bankAccountName)
            etAccountNumber.setText(user.bankAccountNumber)
            etBankName.setText(user.bankName)
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
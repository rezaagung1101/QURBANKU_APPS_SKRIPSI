package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class UpdateProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileJemaahBinding
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileJemaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userViewModel =
            ViewModelProvider(
                this, ViewModelFactory.UserViewModelFactory(userRepository)
            )[UserViewModel::class.java]
        userViewModel.isLoading.observe(this@UpdateProfileJemaahActivity){
            showLoading(it)
        }
        userPreference = UserPreference(this)
        this.setupInformation(userPreference.getJemaahData())
        binding.btnSaveUpdate.setOnClickListener {
            binding.apply {
                val title = resources.getString(R.string.update_profile)
                val message = resources.getString(R.string.update_profile_message)
                val name = etName.text.toString()
                val headName = etHeadFamilyName.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val address = etAddress.text.toString()
                if (validation(phoneNumber, name, address, headName))
                    DialogUtils.showConfirmationDialog(
                        this@UpdateProfileJemaahActivity,
                        title,
                        message
                    )
                    {
                        updateProfile(
                            userPreference.getUid()!!,
                            phoneNumber,
                            name,
                            address,
                            headName
                        )
                    }
            }
        }
    }

    fun updateProfile(
        uid: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String
    ) {
        userViewModel.apply {
            updateJemaahProfile(uid, phoneNumber, name, address, headName)
            updateResult.observe(this@UpdateProfileJemaahActivity) { isSuccess ->
                if (isSuccess) {
                    user.observe(this@UpdateProfileJemaahActivity){ updateduser ->
                        userPreference.saveJemaahPreference(updateduser)
                    }
                    val title = resources.getString(R.string.announcement)
                    val message = resources.getString(R.string.update_profile_success)
                    DialogUtils.showNotificationDialog(this@UpdateProfileJemaahActivity, title, message, ::onBackPressed)
                } else{
                    val title = resources.getString(R.string.announcement)
                    val message = resources.getString(R.string.update_profile_failed)
                    DialogUtils.showNotificationDialog(this@UpdateProfileJemaahActivity, title, message,{})
                }
            }
        }

    }

    fun validation(
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
    ): Boolean {
        var isValid = false
        binding.apply {
            val isPhoneNumberValid = phoneNumber.isNotEmpty()
            val isNameValid = name.isNotEmpty()
            val isAddressValid = address.isNotEmpty()
            val isHeadNameValid = headName.isNotEmpty()
            val editTextPairs = listOf(
                etPhoneNumber to etPhoneNumberLayout,
                etName to etNameLayout,
                etAddress to etAddressLayout,
                etHeadFamilyName to etHeadFamilyNameLayout,
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isPhoneNumberValid && isNameValid && isAddressValid && isHeadNameValid) {
                isValid = true
            }

            val validationList = listOf(
                isPhoneNumberValid to null,
                isNameValid to null,
                isAddressValid to null,
                isHeadNameValid to null,
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(
                            this@UpdateProfileJemaahActivity,
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

    fun setupInformation(user: User) {
        binding.apply{
            etName.setText(user.name)
            etPhoneNumber.setText(user.phoneNumber)
            etAddress.setText(user.address)
            etHeadFamilyName.setText(user.headName)
        }
        val guide = resources.getStringArray(R.array.note_jemaah_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
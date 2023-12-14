package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddShohibulQurbaniBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia.DetailPanitiaAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class AddShohibulQurbaniActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddShohibulQurbaniBinding
    private var animal = Animal()
    private lateinit var animalViewModel: AnimalViewModel
    private val animalRepository = AnimalRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShohibulQurbaniBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.add_shohibul_qurbani)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
        animalViewModel=
            ViewModelProvider(
                this, ViewModelFactory.AnimalViewModelFactory(animalRepository)
            )[AnimalViewModel::class.java]
        animalViewModel.isLoading.observe(this){
            showLoading(it)
        }
        animal = intent.getParcelableExtra<Animal>(Constanta.ANIMAL_DATA) as Animal
        binding.apply {
            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val headName = etHeadFamilyName.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val address = etAddress.text.toString()
                val user = User(
                    uid = "",
                    email = "",
                    headName = headName,
                    name = name,
                    phoneNumber = phoneNumber,
                    admin = false,
                    address = address,
                    bankAccountName = null,
                    latitude = null,
                    longitude = null,
                    bankName = null,
                    bankAccountNumber = null
                )
                val title = resources.getString(R.string.add_shohibul_qurbani)
                val message = resources.getString(R.string.add_shohibul_qurbani_message)
                if (validation(user))
                    DialogUtils.showConfirmationDialog(this@AddShohibulQurbaniActivity, title, message, {
                        addShohibulQurbani(user)
                    })
            }
        }
    }
    fun addShohibulQurbani(user: User){
        animalViewModel.addShohibulQurbani(user, animal.id)
        animalViewModel.addShohibulQurbaniResult.observe(this) { isSuccess ->
            if (isSuccess) {
                val title = resources.getString(R.string.announcement)
                val message = resources.getString(R.string.add_shohibul_qurbani_success)
                DialogUtils.showNotificationDialog(this, title, message){
                    animalViewModel.animal.observe(this){ animal ->
                        val intent = Intent(this@AddShohibulQurbaniActivity, DetailPanitiaAnimalActivity::class.java)
                        intent.putExtra(Constanta.ANIMAL_DATA, animal)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                val title = resources.getString(R.string.announcement)
                val message = resources.getString(R.string.add_shohibul_qurbani_failed)
                DialogUtils.showNotificationDialog(this, title, message, {})
            }
        }
    }

    fun validation(user: User): Boolean {
        var isValid = false
        binding.apply {
            val isPhoneNumberValid = user.phoneNumber.isNotEmpty()
            val isNameValid = user.name.isNotEmpty()
            val isAddressValid = user.address?.isNotEmpty()
            val isHeadNameValid = user.headName.isNotEmpty()
            val editTextPairs = listOf(
                etPhoneNumber to etPhoneNumberLayout,
                etName to etNameLayout,
                etAddress to etAddressLayout,
                etHeadFamilyName to etHeadFamilyNameLayout,
            )
            editTextPairs.forEach { (editText, layout) ->
                Helper.setupTextWatcher(editText, layout)
            }

            if (isPhoneNumberValid && isNameValid && isAddressValid!! && isHeadNameValid) {
                isValid = true
            }

            val validationList = listOf(
                isPhoneNumberValid to null,
                isNameValid to null,
                isAddressValid to null,
                isHeadNameValid to null,
            )

            validationList.forEachIndexed { index, (isValid, errorMessage) ->
                if (!isValid!!) {
                    editTextPairs.getOrNull(index)?.let { (editText, layout) ->
                        Helper.setError(this@AddShohibulQurbaniActivity, editText, layout, errorMessage)
                    }
                }
            }
        }
        return isValid
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@AddShohibulQurbaniActivity, DetailPanitiaAnimalActivity::class.java)
        intent.putExtra(Constanta.ANIMAL_DATA, animal)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
}
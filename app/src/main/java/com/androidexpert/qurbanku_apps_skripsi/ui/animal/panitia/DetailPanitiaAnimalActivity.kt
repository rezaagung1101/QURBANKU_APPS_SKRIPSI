package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.adapter.animal.ShohibulQurbaniAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailPanitiaAnimalBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.bumptech.glide.Glide


class DetailPanitiaAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPanitiaAnimalBinding
    private var shohibulQurbanAmount = 0
    private var jointVentureAmount = 0
    private var animal = Animal()
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    private lateinit var animalViewModel: AnimalViewModel
    private val animalRepository = AnimalRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPanitiaAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_animal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        animal = intent.getParcelableExtra<Animal>(Constanta.ANIMAL_DATA) as Animal
        shohibulQurbanAmount = animal.idShohibulQurbaniList?.size ?: 0
        jointVentureAmount = animal.jointVentureAmount
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        animalViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AnimalViewModelFactory(animalRepository)
        )[AnimalViewModel::class.java]
        setupInformation(animal)
        binding.btnConfirmStatus.setOnClickListener {
            val title = resources.getString(R.string.animal_confirm_status)
            val message = resources.getString(R.string.animal_confirm_status_message)
            DialogUtils.showConfirmationDialog(this, title, message, ::updateAnimalStatus)
        }
    }

    fun setupInformation(animal: Animal) {
        binding.apply {
            with(animal) {
                val totalPrice = price + operationalCosts
                val costRequired = totalPrice / jointVentureAmount
                val availability = jointVentureAmount - (idShohibulQurbaniList?.size ?: 0)
                tvDescriptionAnimalName.text =
                    resources.getString(R.string.description_animal_name, speciesName)
                tvVarietyName.text = varietyName
                tvWeight.text =
                    if (weight % 1 == 0.0) resources.getString(
                        R.string.weight_value,
                        String.format("%.0f kg", weight)
                    )
                    else resources.getString(
                        R.string.weight_value,
                        String.format("%.1f kg", weight)
                    )
                tvColor.text = color
                tvPrice.text =
                    resources.getString(R.string.price_2, Helper.parseNumberFormat(price))
                tvOperationalCost.text = resources.getString(
                    R.string.price_2,
                    Helper.parseNumberFormat(operationalCosts)
                )
                tvTotalPrice.text =
                    resources.getString(R.string.price_2, Helper.parseNumberFormat(totalPrice))
                tvJointVentureAmount.text = jointVentureAmount.toString()
                tvTotalCostRequired.text =
                    resources.getString(R.string.price_2, Helper.parseNumberFormat(costRequired))
                tvHolidayDateValue.text = Helper.getLongSimpleDate(qurbaniTimeMillisecond)
                tvNoteValue.text =
                    if (note!!.length != 0) note.toString()
                    else resources.getString(R.string.null_note)
                tvAvailability.text =
                    resources.getString(R.string.availability_number, availability)
                tvAnimalStatus.text =
                    if (status) resources.getString(R.string.animal_status_executed)
                    else resources.getString(R.string.animal_status_not_executed)
                Glide.with(this@DetailPanitiaAnimalActivity)
                    .load(animal.photoUrl)
                    .into(ivAnimal)
                if (idShohibulQurbaniList != null) {
                    setShohibulQurbaniData(idShohibulQurbaniList)
                }
                if (availability != 0 || status) {
                    btnConfirmStatus.setBackgroundColor(resources.getColor(R.color.disabled_background))
                    btnConfirmStatus.setTextColor(resources.getColor(R.color.disabled_text))
                    btnConfirmStatus.isEnabled = false
                }
                if (availability == 0) {
                    btnAddShohibulQurban.isEnabled = false
                }
            }
        }
        animalViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    fun setShohibulQurbaniData(idJemaah: List<String>) {
        val layoutManager = LinearLayoutManager(
            this@DetailPanitiaAnimalActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        val shohibulQurbaniList = arrayListOf<User>()
        binding.rvShohibulQurbanList.layoutManager = layoutManager
        userViewModel.getJemaahList(idJemaah)
        userViewModel.listUser.observe(this@DetailPanitiaAnimalActivity) { listJemaah ->
            listJemaah.forEach { user ->
                shohibulQurbaniList.add(user)
            }
            binding.rvShohibulQurbanList.adapter = ShohibulQurbaniAdapter(shohibulQurbaniList, true)
        }
    }

    fun deleteAnimal() {
        animalViewModel.deleteAnimal(animal.id, animal.photoUrl)
        animalViewModel.deleteAnimalResult.observe(this) { isSuccess ->
            val title = resources.getString(R.string.announcement)
            val message = if (isSuccess) {
                resources.getString(R.string.delete_animal_success)
            } else {
                resources.getString(R.string.delete_animal_failed)
            }
            DialogUtils.showNotificationDialog(this, title, message) {
                if (isSuccess) {
                    startActivity(Intent(this, MainPanitiaActivity::class.java))
                    finish()
                }
            }
        }
    }

    fun updateAnimalStatus() {
        animalViewModel.updateAnimalStatus(animal.id)
        animalViewModel.updateAnimalStatusResult.observe(this) { isSuccess ->
            val title = resources.getString(R.string.announcement)
            val message = if (isSuccess) {
                resources.getString(R.string.animal_confirm_status_success)
            } else {
                resources.getString(R.string.animal_confirm_status_failed)
            }
            DialogUtils.showNotificationDialog(this, title, message) {
                if (isSuccess) {
                    animalViewModel.animal.observe(this) { animalData ->
                        val intent = getIntent()
                        intent.putExtra(Constanta.ANIMAL_DATA, animalData)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                }
            }
        }
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_panitia_animal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_animal -> {
                val title = resources.getString(R.string.delete_animal_data)
                val message = resources.getString(R.string.animal_delete_message)
                if (shohibulQurbanAmount == 0) DialogUtils.showConfirmationDialog(
                    this,
                    title,
                    message,
                    ::deleteAnimal
                )
                else Toast.makeText(
                    this,
                    resources.getString(R.string.animal_delete_failed),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        animalViewModel.apply {
            animal.removeObservers(this@DetailPanitiaAnimalActivity)
            updateAnimalStatusResult.removeObservers(this@DetailPanitiaAnimalActivity)
            deleteAnimalResult.removeObservers(this@DetailPanitiaAnimalActivity)
            addAnimalResult.removeObservers(this@DetailPanitiaAnimalActivity)
            isLoading.removeObservers(this@DetailPanitiaAnimalActivity)
        }
        userViewModel.listUser.removeObservers(this)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
package com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.adapter.animal.ShohibulQurbaniAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailJemaahAnimalBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah.AddTransactionActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.bumptech.glide.Glide

class DetailJemaahAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJemaahAnimalBinding
    private var shohibulQurbanAmount: Int = 0
    private var jointVentureAmount: Int = 0
    private var animalData = Animal()
    private var masjidData = User()
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJemaahAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animalData = intent.getParcelableExtra<Animal>(Constanta.ANIMAL_DATA) as Animal
        masjidData = intent.getParcelableExtra<User>(Constanta.USER_DATA) as User
        supportActionBar?.title = getString(R.string.detail_animal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory.UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
        setupInformation(animalData, masjidData)
    }


    fun setupInformation(animal: Animal, masjid: User) {
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
                Glide.with(this@DetailJemaahAnimalActivity)
                    .load(animal.photoUrl)
                    .into(ivAnimal)
                if (idShohibulQurbaniList != null) {
                    setShohibulQurbaniData(idShohibulQurbaniList)
                }
                if (availability != 0 && !status) {
                    binding.btnBuyAnimal.setOnClickListener {
                        val intent = Intent(this@DetailJemaahAnimalActivity, AddTransactionActivity::class.java)
                        intent.putExtra(Constanta.ANIMAL_DATA, animal)
                        intent.putExtra(Constanta.USER_DATA, masjid)
                        startActivity(intent)
                    }
                }
                if (availability == 0 || status) {
                    binding.btnBuyAnimal.isEnabled = false
                    binding.btnBuyAnimal.setBackgroundColor(resources.getColor(R.color.disabled_background))
                    binding.btnBuyAnimal.setTextColor(resources.getColor(R.color.disabled_text))
                }
            }
        }
    }

    fun setShohibulQurbaniData(idJemaah: List<String>) {
        val layoutManager = LinearLayoutManager(
            this@DetailJemaahAnimalActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        val shohibulQurbaniList = arrayListOf<User>()
        binding.rvShohibulQurbanList.layoutManager = layoutManager
        userViewModel.getJemaahList(idJemaah)
        userViewModel.listUser.observe(this@DetailJemaahAnimalActivity) { listJemaah ->
            listJemaah.forEach { user ->
                shohibulQurbaniList.add(user)
            }
            binding.rvShohibulQurbanList.adapter = ShohibulQurbaniAdapter(shohibulQurbaniList, false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
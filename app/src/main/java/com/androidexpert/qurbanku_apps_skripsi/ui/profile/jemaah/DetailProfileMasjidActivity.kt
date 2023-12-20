package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.adapter.animal.AnimalJemaahAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailProfileMasjidBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference

class DetailProfileMasjidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileMasjidBinding
    private var masjidData = MasjidUser()
    private var masjid = User()
    private var animalList  = listOf<Animal>()
    private lateinit var userPreference: UserPreference
    private lateinit var animalViewModel: AnimalViewModel
    private val animalRepository = AnimalRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileMasjidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.profile_masjid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        animalViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AnimalViewModelFactory(animalRepository)
        )[AnimalViewModel::class.java]
        userPreference = UserPreference(this)
        masjidData = intent.getParcelableExtra<MasjidUser>(Constanta.USER_DATA) as MasjidUser
        masjid = masjidData.user!!
        animalList = masjidData.listAnimal!!
        setupInformation(masjid, animalList)
        binding.fabCallByWhatsapp.setOnClickListener {
            sendWhatsAppMessage(Helper.convertToInternationalFormat(masjid.phoneNumber))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setupInformation(masjid: User?, animalList: List<Animal>?) {
        binding.apply {
            if (masjid != null) {
                with(masjid) {
                    tvMasjidName.text = resources.getString(R.string.name_masjid_value, name)
                    tvHeadValue.text = headName
                    tvContactPersonValue.text = phoneNumber
                    tvAccountNumberValue.text = bankAccountNumber
                    tvBankValue.text = bankName
                    tvAccountNameValue.text = bankAccountName
                    tvAddressValue.text = Helper.parseCompleteAddress(
                        this@DetailProfileMasjidActivity,
                        latitude!!,
                        longitude!!
                    )
                    tvAddressValue.paintFlags =
                        tvAddressValue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    tvAddressValue.setOnClickListener {
                        openGmapsByLocation(latitude, longitude)
                    }
                }
            }
            if (!animalList.isNullOrEmpty()) {
                setAnimalList(masjid!!, animalList)
                swipeRefresh.setOnRefreshListener {
                    animalViewModel.getAnimalList(masjid!!.uid)
                    animalViewModel.listAnimal.observe(this@DetailProfileMasjidActivity) { animalList ->
                        if (animalList != null) setAnimalList(masjid, animalList)
                        else binding.tvNullAnimal.alpha = 1f
                    }
                    binding.swipeRefresh.isRefreshing = true
                    // Use a Handler to post a delayed action
                    Handler().postDelayed({
                        binding.swipeRefresh.isRefreshing = false
                        binding.rvAnimalList.smoothScrollToPosition(0)
                    }, 1000)
                }
            } else binding.tvNullAnimal.alpha = 1f
        }
    }

    fun sendWhatsAppMessage(phoneNumber: String){
        try {
            val message = "Halo,%20saya%20${userPreference.getName()}%20ingin%20bertanya%20mengenai%20hewan%20kurban%20milik%20masjid%20${masjid.name}."
            val uri = Uri.parse("https://wa.me/$phoneNumber?text=$message")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Terdapat kesalahan, tidak dapat menghubungi lewat WhatsApp", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun setAnimalList(masjid: User, animalList: List<Animal>){
        val arrayListAnimal = arrayListOf<Animal>()
        animalList.forEach{
            arrayListAnimal.add(it)
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAnimalList.layoutManager = layoutManager
        val adapter = AnimalJemaahAdapter(masjid, arrayListAnimal)
        binding.rvAnimalList.adapter = adapter
    }

    fun openGmapsByLocation(latitude: Double, longitude: Double){
        val location = "${latitude},${longitude}"
        val gmmIntentUri = Uri.parse("geo:0,0?q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager).let {
            startActivity(mapIntent)
        }
    }

    override fun onDestroy() {
        animalViewModel.listAnimal.removeObservers(this)
        super.onDestroy()
    }
}
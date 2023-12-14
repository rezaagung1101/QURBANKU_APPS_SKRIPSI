package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailTransactionJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ImageDisplayActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah.DetailJemaahAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah.DetailProfileMasjidActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.bumptech.glide.Glide

class DetailTransactionJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionJemaahBinding
    private var transaction = TransactionDetail()
    private lateinit var animalViewModel: AnimalViewModel
    private val animalRepository = AnimalRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionJemaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        animalViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AnimalViewModelFactory(animalRepository)
        )[AnimalViewModel::class.java]
        transaction =
            intent.getParcelableExtra<TransactionDetail>(Constanta.TRANSACTION_DATA) as TransactionDetail
        setupInformation(transaction)
    }

    fun setupInformation(transactionDetail: TransactionDetail) {
        val transaction = transactionDetail.transaction
        val masjid = transactionDetail.masjid
        val animal = transactionDetail.animal
        animalViewModel.getAnimalList(masjid!!.uid)
        binding.apply {
            with(transaction) {
                Glide.with(this@DetailTransactionJemaahActivity)
                    .load(this!!.photoUrl)
                    .into(ivTransaction)
                tvTransactionDate.text = Helper.getLongSimpleDateTransaction(createdTimeMillisecond)
                if (status == true) {
                    tvTransactionStatus.text = resources.getString(R.string.accepted)
                    tvTransactionStatus.setTextColor(resources.getColor(R.color.green_main))
                } else if (status == false) {
                    tvTransactionStatus.text = resources.getString(R.string.rejected)
                    tvTransactionStatus.paintFlags = tvTransactionStatus.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvTransactionStatus.setTextColor(resources.getColor(R.color.danger))
                }
                if(note != null && note.length >1)
                    tvNote.text = note.toString()
                else tvNote.text = resources.getString(R.string.null_note)
            }
            with(masjid){
                tvMasjidName.text = resources.getString(R.string.name_masjid_value, this!!.name)
                tvBankName.text = bankName
                tvAccountNumber.text = bankAccountNumber
                tvAccountName.text = bankAccountName
            }
            with(animal){
                val totalPrice = this!!.price + operationalCosts
                val costRequired = totalPrice / jointVentureAmount
                tvTransferNominal.text = resources.getString(
                    R.string.price_2,
                    Helper.parseNumberFormat(costRequired)
                )
                tvAnimalSpecies.text = speciesName
                tvAnimalVariety.text = varietyName
                tvAnimalWeight.text =
                    if (weight % 1 == 0.0) resources.getString(
                        R.string.weight_value,
                        String.format("%.0f kg", weight)
                    )
                    else resources.getString(
                        R.string.weight_value,
                        String.format("%.1f kg", weight)
                    )
                tvAnimalColor.text = color
                tvCategory.text =
                    if (jointVentureAmount > 1) resources.getString(R.string.joint_venture)
                    else resources.getString(R.string.purchase_category)
            }
            btnMasjidDetail.setOnClickListener {
                animalViewModel.listAnimal.observe(this@DetailTransactionJemaahActivity){ animalList ->
                    val intent = Intent(this@DetailTransactionJemaahActivity, DetailProfileMasjidActivity::class.java)
                    val masjidData = MasjidUser(masjid, animalList)
                    intent.putExtra(Constanta.USER_DATA, masjidData)
                    startActivity(intent)
                }
            }
            btnAnimalDetail.setOnClickListener {
                val intent = Intent(this@DetailTransactionJemaahActivity, DetailJemaahAnimalActivity::class.java)
                intent.putExtra(Constanta.ANIMAL_DATA, animal)
                intent.putExtra(Constanta.USER_DATA, masjid)
                startActivity(intent)
            }
            ivTransaction.setOnClickListener {
                val intent = Intent(this@DetailTransactionJemaahActivity, ImageDisplayActivity::class.java)
                intent.putExtra(Constanta.photoUrl, transaction!!.photoUrl)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        animalViewModel.listAnimal.removeObservers(this)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }
}
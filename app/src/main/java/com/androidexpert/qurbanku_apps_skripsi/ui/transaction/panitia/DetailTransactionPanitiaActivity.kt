package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.remote.TransactionRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailTransactionPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ImageDisplayActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia.DetailPanitiaAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia.DetailProfileJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.TransactionViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.bumptech.glide.Glide

class DetailTransactionPanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionPanitiaBinding
    private var transaction = TransactionDetail()
    private lateinit var transactionViewModel: TransactionViewModel
    private val transactionRepository = TransactionRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionPanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        transactionViewModel = ViewModelProvider(this, ViewModelFactory.TransactionViewModelFactory(transactionRepository))[TransactionViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.detail_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        transaction =
            intent.getParcelableExtra<TransactionDetail>(Constanta.TRANSACTION_DATA) as TransactionDetail
        setupInformation(transaction)
        val title = resources.getString(R.string.transaction_confirmation)
        val idJemaah = transaction.jemaah!!.uid
        val idAnimal = transaction.animal!!.id
        binding.btnConfirmAccept.setOnClickListener {
            val message = resources.getString(R.string.transaction_confirmation_accept_message)
            DialogUtils.showConfirmationDialog(this, title, message, { confirmTransaction(idJemaah, idAnimal,true) })
        }
        binding.btnConfirmReject.setOnClickListener {
            val message = resources.getString(R.string.transaction_confirmation_reject_message)
            DialogUtils.showConfirmationDialog(this, title, message, { confirmTransaction(idJemaah, idAnimal,false) })
        }
        transactionViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    fun confirmTransaction(idJemaah: String, idAnimal: String, status: Boolean) {
        val idTransaction = transaction.transaction!!.id
        val note = binding.etNote.text.toString()
        transactionViewModel.confirmTransaction(idJemaah, idAnimal, idTransaction, status, note)
        transactionViewModel.confirmTransactionResult.observe(this){ isSuccess ->
            if (isSuccess==true){
                transactionViewModel.transactionDetail.observe(this){ transaction ->
                    showDialog(isSuccess, transaction.transaction!!.status, transaction)
                }
            } else {
                showDialog(false, null, null)
            }
        }
    }

    fun showDialog(isSuccess: Boolean, status: Boolean?, transactionDetail: TransactionDetail?){
        val messageResId =
            if (status!! && isSuccess) R.string.transaction_accepted
            else if (!status && isSuccess) R.string.transaction_rejected
            else R.string.confirmation_transaction_error_message
        val title = resources.getString(R.string.announcement)
        val message = resources.getString(messageResId)

        DialogUtils.showNotificationDialog(this, title, message) {
            if(isSuccess){
                val intent = getIntent()
                intent.putExtra(Constanta.TRANSACTION_DATA, transactionDetail)
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }
    }

    fun setupInformation(transactionDetail: TransactionDetail) {
        val transaction = transactionDetail.transaction
        val animal = transactionDetail.animal
        val jemaah = transactionDetail.jemaah
        if (transaction != null && animal != null && jemaah != null) {
            binding.apply {
                with(transaction) {
                    if (status != null) {
                        btnConfirmAccept.isEnabled = false
                        btnConfirmAccept.setBackgroundColor(resources.getColor(R.color.disabled_background))
                        btnConfirmAccept.setTextColor(resources.getColor(R.color.disabled_text))
                        btnConfirmReject.isEnabled = false
                        btnConfirmReject.setBackgroundColor(resources.getColor(R.color.disabled_background))
                        btnConfirmReject.setTextColor(resources.getColor(R.color.disabled_text))
                        etNote.isEnabled = false
                        if (note != null && note.length > 0) etNote.hint = note
                        else etNote.hint = resources.getString(R.string.null_note)
                        etNoteLayout.hint = ""
                        etNote.setBackgroundColor(resources.getColor(R.color.disabled_background))
                    }
                    if (status == true) {
                        tvTransactionStatus.text = resources.getString(R.string.accepted)
                        tvTransactionStatus.setTextColor(resources.getColor(R.color.green_main))
                    } else if (status == false) {
                        tvTransactionStatus.text = resources.getString(R.string.rejected)
                        tvTransactionStatus.paintFlags = tvTransactionStatus.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        tvTransactionStatus.setTextColor(resources.getColor(R.color.danger))
                    }
                    Glide.with(this@DetailTransactionPanitiaActivity)
                        .load(photoUrl)
                        .into(ivTransaction)
                    tvTransactionDate.text =
                        Helper.getLongSimpleDateTransaction(createdTimeMillisecond)
                }
                with(animal){
                    val totalPrice = this!!.price + operationalCosts
                    val costRequired = totalPrice / jointVentureAmount
                    tvCostRequired.text = resources.getString(
                        R.string.price_2,
                        Helper.parseNumberFormat(costRequired)
                    )
                    tvAnimalSpecies.text = speciesName
                    tvAnimalVariety.text = varietyName
                    tvCategory.text =
                        if (jointVentureAmount > 1) resources.getString(R.string.joint_venture)
                        else resources.getString(R.string.purchase_category)
                }
                with(jemaah){
                    tvEmail.text = email
                    tvJemaahName.text = name
                    tvJemaahPhoneNumber.text = phoneNumber
                }
                btnJemaahDetail.setOnClickListener {
                    val intent = Intent(this@DetailTransactionPanitiaActivity, DetailProfileJemaahActivity::class.java)
                    intent.putExtra(Constanta.USER_DATA, jemaah)
                    startActivity(intent)
                }
                btnAnimalDetail.setOnClickListener {
                    val intent = Intent(this@DetailTransactionPanitiaActivity, DetailPanitiaAnimalActivity::class.java)
                    intent.putExtra(Constanta.ANIMAL_DATA, animal)
                    startActivity(intent)
                }
                ivTransaction.setOnClickListener {
                    val intent = Intent(this@DetailTransactionPanitiaActivity, ImageDisplayActivity::class.java)
                    intent.putExtra(Constanta.photoUrl, transaction.photoUrl)
                    startActivity(intent)
                }
            }
        }
    }
    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }


}
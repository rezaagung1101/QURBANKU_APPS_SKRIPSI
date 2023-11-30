package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailTransactionPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.MainJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class DetailTransactionPanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionPanitiaBinding
    private var status: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionPanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         * masukkan status transaksi ke var status
         */
        supportActionBar?.title = getString(R.string.detail_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupInformation()
        val title = resources.getString(R.string.transaction_confirmation)
        binding.btnConfirmAccept.setOnClickListener {
            val message = resources.getString(R.string.transaction_confirmation_accept_message)
            DialogUtils.showNotificationDialog(this, title, message, { confirmTransaction(true) })
        }
        binding.btnConfirmReject.setOnClickListener {
            val message = resources.getString(R.string.transaction_confirmation_reject_message)
            DialogUtils.showNotificationDialog(this, title, message, { confirmTransaction(false) })
        }
    }

    fun getDetailTransaction(){

    }

    fun confirmTransaction(status: Boolean) {
        if (status == true) {
            Toast.makeText(
                this,
                resources.getString(R.string.transaction_accepted),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.transaction_rejected),
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
        overridePendingTransition(0, 0)
        startActivity(getIntent());
        overridePendingTransition(0, 0)
    }

    fun setupInformation() {
        if (status == true) {
            binding.apply {
                btnConfirmAccept.isEnabled = false
                btnConfirmAccept.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnConfirmAccept.setTextColor(resources.getColor(R.color.disabled_text))
                btnConfirmReject.isEnabled = false
                btnConfirmReject.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnConfirmReject.setTextColor(resources.getColor(R.color.disabled_text))
                etNote.isEnabled = false
                val tempNote = "telah dikonfirmasi" //GANTI DENGAN NOTE DARI TRANSAKSI
                etNote.hint = tempNote
                etNoteLayout.hint = ""
                etNote.setBackgroundColor(resources.getColor(R.color.disabled_background))
            }

        } else {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }


}
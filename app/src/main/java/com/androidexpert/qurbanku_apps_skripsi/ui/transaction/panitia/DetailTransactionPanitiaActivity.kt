package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailTransactionPanitiaBinding

class DetailTransactionPanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionPanitiaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionPanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
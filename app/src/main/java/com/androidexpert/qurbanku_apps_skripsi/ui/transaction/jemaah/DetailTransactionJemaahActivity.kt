package com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailTransactionJemaahBinding

class DetailTransactionJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionJemaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionJemaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
        finish()
    }
}
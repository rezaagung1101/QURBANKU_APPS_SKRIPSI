package com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfileJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class UpdateProfileJemaahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileJemaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileJemaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setupInformation()
        binding.btnSaveUpdate.setOnClickListener {
            val title= resources.getString(R.string.update_profile)
            val message= resources.getString(R.string.update_profile_message)
            DialogUtils.showConfirmationDialog(this, title, message, ::updateProfile)
        }
    }

    fun updateProfile(){
//        finish()
        onBackPressed()
    }

    fun setupInformation(){
        val guide = resources.getStringArray(R.array.note_jemaah_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
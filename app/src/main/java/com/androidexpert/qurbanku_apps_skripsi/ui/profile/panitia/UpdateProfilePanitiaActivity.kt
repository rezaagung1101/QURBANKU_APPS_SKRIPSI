package com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityUpdateProfilePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils

class UpdateProfilePanitiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfilePanitiaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfilePanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setupInformation()
        binding.btnSaveUpdate.setOnClickListener {
            val title= resources.getString(R.string.update_profile)
            val message= resources.getString(R.string.update_profile_message)
            DialogUtils.showConfirmationDialog(this, title, message, ::updatePanitiaProfile)
        }
    }
    fun updatePanitiaProfile(){
        Toast.makeText(
            this,
            resources.getString(
                R.string.update_profile
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setupInformation() {
        val guide = resources.getStringArray(R.array.note_panitia_signUp).joinToString("\n")
        binding.tvNoteValue.text = guide
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
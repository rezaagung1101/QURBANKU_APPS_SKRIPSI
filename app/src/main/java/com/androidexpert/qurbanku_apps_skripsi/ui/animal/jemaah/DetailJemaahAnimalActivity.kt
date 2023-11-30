package com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailJemaahAnimalBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah.AddTransactionActivity

class DetailJemaahAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJemaahAnimalBinding
    private var shohibulQurbanAmount: Int = 0
    private var jointVentureAmount: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJemaahAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_animal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupInformation()
    }

    fun setupInformation(){
        if (shohibulQurbanAmount<jointVentureAmount){
            binding.btnBuyAnimal.setOnClickListener {
                val intent = Intent(this, AddTransactionActivity::class.java)
                //kirim data
                startActivity(intent)
            }
        }else{
            binding.btnBuyAnimal.isEnabled = false
            binding.btnBuyAnimal.setBackgroundColor(resources.getColor(R.color.disabled_background))
            binding.btnBuyAnimal.setTextColor(resources.getColor(R.color.disabled_text))
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
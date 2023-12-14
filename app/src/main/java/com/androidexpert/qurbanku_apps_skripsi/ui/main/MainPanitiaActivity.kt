package com.androidexpert.qurbanku_apps_skripsi.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityMainPanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia.AddAnimalFragment
import com.androidexpert.qurbanku_apps_skripsi.utils.DatePickerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPanitiaActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityMainPanitiaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPanitiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main_panitia)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_add_animal,
                R.id.navigation_transaction,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onBackPressed() {
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d("MainPanitiaActivity", "Selected Date: $dayOfMonth/$month/$year")
    }


}
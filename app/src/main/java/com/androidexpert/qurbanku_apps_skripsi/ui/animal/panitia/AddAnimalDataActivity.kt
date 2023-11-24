package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityAddAnimalDataBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DialogUtils
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper

class AddAnimalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAnimalDataBinding
    private var jointVentureValue: Int = 1
    private var animalSpecies: String = ""
    private var dueDateMillis: Long = System.currentTimeMillis()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAnimalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dueDateMillis = intent.getLongExtra(DATE_DATA, System.currentTimeMillis())
        Toast.makeText(
            this,
            Helper.convertMillisToString(dueDateMillis),
            Toast.LENGTH_LONG
        ).show()
        setupSpinner()
        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture))
        binding.btnSave.setOnClickListener {
            val title = resources.getString(R.string.save_data_animal)
            val message = resources.getString(R.string.save_data_animal_message)
            DialogUtils.showConfirmationDialog(this,title, message,::addAnimal)
        }
    }

    private fun addAnimal(){
        //set the algorithm for viewmodel
        startActivity(Intent(this, DetailPanitiaAnimalActivity::class.java))
    }

    private fun setupSpinner() {
        val animalList = resources.getStringArray(R.array.spinner_qurban_animal)
        val animalAdapter =
            ArrayAdapter(this, R.layout.spinner_item, animalList)
        animalAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.btnSpinnerAnimal.adapter = animalAdapter
        binding.btnSpinnerAnimal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    animalSpecies = animalList[position].toString()
                    if (position == 1 || position == 2) {
                        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture_disabled))
                    }else{
                        setupSpinnerJointVenture(resources.getStringArray(R.array.spinner_joint_venture))
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    animalSpecies = animalList[0].toString()
                }
            }
    }
    private fun setupSpinnerJointVenture(jointVentureList: Array<String>){
        val array = jointVentureList
        val jointVentureAdapter =
            ArrayAdapter(this, R.layout.spinner_item, array)
        jointVentureAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.btnSpinnerJointVenture.adapter = jointVentureAdapter
        binding.btnSpinnerJointVenture.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    jointVentureValue = array[position].toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    jointVentureValue = array[0].toInt()
                }
            }
    }
    companion object{
        const val DATE_DATA = "DATE"
    }
}
package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentAddAnimalBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DatePickerFragment
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import java.util.Calendar


class AddAnimalFragment : Fragment(), DatePickerFragment.DialogDateListener,
    DatePickerFragment.FragmentDateListener {
    private lateinit var binding: FragmentAddAnimalBinding

    //    private val animalViewModel: AnimalViewModel by viewModels()
    private var dueDateMillis: Long = System.currentTimeMillis()
    private val defaultStatus: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddAnimalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setupInformation(defaultStatus)

        binding.cardAddDate.setOnClickListener {
            showDatePicker()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDatePicker() { //parameter view: View dihapus
        val dialogFragment = DatePickerFragment()
        dialogFragment.setListener(this)
        dialogFragment.setFragmentListener(this)
        dialogFragment.show(requireFragmentManager(), "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        //just implement caused by the rule that implementing the DatePickerFragment in MainPanitiaActivity
    }

    fun setupInformation(status: Boolean) {
        binding.apply{
            btnAddDetailAnimal.isEnabled = status
            if (status != true) {
                btnAddDetailAnimal.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnAddDetailAnimal.setTextColor(resources.getColor(R.color.disabled_text))
            } else {
                btnAddDetailAnimal.setOnClickListener {
                    //move to detail
                    //send dueDateMillis
                    val intent = Intent(requireContext(), AddAnimalDataActivity::class.java)
                    intent.putExtra(AddAnimalDataActivity.DATE_DATA, dueDateMillis)
                    startActivity(intent)
                }
            }
        }
        val guide = resources.getStringArray(R.array.add_animal_guide).joinToString("\n")
        binding.tvGuideValue.text = guide
    }

    override fun onFragmentDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        binding.tvEidAlAdhaDate.text = Helper.getLongSimpleDate(calendar.timeInMillis)
        dueDateMillis = calendar.timeInMillis
        binding.btnAddDetailAnimal.setBackgroundColor(resources.getColor(R.color.green_main))
        binding.btnAddDetailAnimal.setTextColor(resources.getColor(R.color.white))
        setupInformation(true)
    }
}
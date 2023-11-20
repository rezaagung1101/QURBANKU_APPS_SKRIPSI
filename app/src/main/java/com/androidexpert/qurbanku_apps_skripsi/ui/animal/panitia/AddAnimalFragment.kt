package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentAddAnimalBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddAnimalFragment : Fragment(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: FragmentAddAnimalBinding
    private var dueDateMillis: Long = System.currentTimeMillis()
    private var status: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddAnimalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnAddDate.setOnClickListener {
                showDatePicker()
            }
            tvEidAlAdhaDate.setOnClickListener {
                showDatePicker()
            }
            if (status != true) {
                btnAddDetailAnimal.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnAddDetailAnimal.setTextColor(resources.getColor(R.color.disabled_text))
                btnAddDetailAnimal.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.add_date_advice),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                btnAddDetailAnimal.setBackgroundColor(resources.getColor(R.color.green_main))
                btnAddDetailAnimal.setTextColor(resources.getColor(R.color.white))
                btnAddDetailAnimal.setOnClickListener {
                    //move to detail
                    //send dueDateMillis
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDatePicker() { //parameter view: View dihapus
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(childFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvEidAlAdhaDate.text = dateFormat.format(calendar.time)
        dueDateMillis = calendar.timeInMillis
        this.status = true
    }
}
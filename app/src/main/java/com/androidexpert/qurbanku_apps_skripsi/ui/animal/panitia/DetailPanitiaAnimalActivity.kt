package com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityDetailPanitiaAnimalBinding


class DetailPanitiaAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPanitiaAnimalBinding
    private var shohibulQurbanAmount = 0 //jangan lupa diset jumlahnya
    private var jointVentureAmount = 0 //jangan lupa diset jumlahnya
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPanitiaAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_animal)
        binding.btnConfirmStatus.setOnClickListener {
            //CEK DULU KALO KUOTA PATUNGAN MASIH ADA MAKA BATAL
            if (shohibulQurbanAmount != jointVentureAmount)
                Toast.makeText(
                    this,
                    resources.getString(R.string.animal_confirm_status_failed, (jointVentureAmount-shohibulQurbanAmount)),
                    Toast.LENGTH_SHORT
                ).show()
            else {
                val title = resources.getString(R.string.animal_confirm_status)
                val message = resources.getString(R.string.animal_confirm_status_message)
                showConfirmationDialog(confirm, title, message)
            }
        }
    }

    private fun deleteAnimal() {
        Toast.makeText(
            this,
            resources.getString(R.string.delete_animal_success),
            Toast.LENGTH_SHORT
        ).show()
        //set delete
        //move to home
    }

    private fun updateStatus() {
        Toast.makeText(
            this,
            resources.getString(R.string.animal_confirm_status_success),
            Toast.LENGTH_SHORT
        ).show()
        //reload page with latest data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_panitia_animal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_animal -> {
                val title = resources.getString(R.string.delete_animal_data)
                val message = resources.getString(R.string.animal_delete_message)
                if (shohibulQurbanAmount == 0) showConfirmationDialog(delete, title, message)
                else Toast.makeText(
                    this,
                    resources.getString(R.string.animal_delete_failed),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog(action: String, title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(resources.getString(R.string.next2)) { _, _ ->
            if (action == delete) deleteAnimal()
            if (action == confirm) updateStatus()
        }
        builder.setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
            //
        }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.danger))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.green_main))
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
    }

    companion object {
        val delete: String = "DELETE"
        val confirm: String = "CONFIRM"
    }
}
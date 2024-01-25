package com.androidexpert.qurbanku_apps_skripsi.adapter.user

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardMasjidItemBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.jemaah.DetailProfileMasjidActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper


class MasjidListAdapter(private val listData: ArrayList<MasjidUser>) :
    RecyclerView.Adapter<MasjidListAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardMasjidItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardMasjidItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: MasjidUser) {
            with(holder.itemView) {
                holder.binding.apply {
                    val userData = data.user
                    val animalList = data.listAnimal
                    if (userData != null) {
                        tvName.text = resources.getString(R.string.name_masjid_value, userData.name)
                        if (userData.latitude != null && userData.longitude != null) {
                            tvAddress.text =
                                Helper.parseCompleteAddress(context, userData.latitude, userData.longitude)
                        }
                        if (!animalList.isNullOrEmpty()) {
                            var availableAnimal = 0
                            animalList.forEach { animal ->
                                if (animal.jointVentureAmount - (animal.idShohibulQurbaniList?.size ?: 0) > 0)
                                    availableAnimal += 1
                            }
                            tvAvailability.text = availableAnimal.toString()
                        } else{
                            tvAvailability.text = "0"
                        }
                        tvAvailability.paintFlags = tvAvailability.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    }
                }
                this.setOnClickListener {
                    val intent = Intent(context, DetailProfileMasjidActivity::class.java)
                    intent.putExtra(Constanta.USER_DATA, data)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            }

        }
        bind(listData[position])
    }


    override fun getItemCount() = listData.size

}
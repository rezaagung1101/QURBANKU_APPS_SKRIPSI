package com.androidexpert.qurbanku_apps_skripsi.adapter.animal

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardDisplayAnimalItemBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.jemaah.DetailJemaahAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.panitia.DetailPanitiaAnimalActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper
import com.bumptech.glide.Glide

class AnimalJemaahAdapter(private val masjidData: User, private val listData: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalJemaahAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardDisplayAnimalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardDisplayAnimalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: Animal) {
            with(holder.itemView) {
                holder.binding.apply {
                    with(data) {
                        Glide.with(context)
                            .load(photoUrl)
                            .into(ivBanner)
                        val totalPrice = price + operationalCosts
                        val costRequired = totalPrice / jointVentureAmount
                        val availability = jointVentureAmount - (idShohibulQurbaniList?.size ?: 0)
                        tvSpeciesName.text = speciesName
                        tvVarietyValue.text = varietyName
                        tvWeightValue.text =
                            if (weight % 1 == 0.0) resources.getString(
                                R.string.weight_value,
                                String.format("%.0f kg", weight)
                            )
                            else resources.getString(
                                R.string.weight_value,
                                String.format("%.1f kg", weight)
                            )
                        tvJointVentureValue.text = jointVentureAmount.toString()
                        tvPrice.text = resources.getString(
                            R.string.price_2,
                            Helper.parseNumberFormat(costRequired)
                        )
                        if (availability==0 && status){
                            tvAvailability.text = resources.getString(R.string.animal_status_executed)
                            cardContainer.setBackgroundColor(resources.getColor(R.color.disabled_background))
                        }else if(availability==0 && !status){
                            tvAvailability.text =
                                resources.getString(R.string.animal_status_sold)
                            cardContainer.setBackgroundColor(resources.getColor(R.color.green_sold_status))
                        } else{
                            tvAvailability.text =
                                resources.getString(R.string.availability_number, availability)
                        }
                        tvAvailability.paintFlags = tvAvailability.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    }
                }
                this.setOnClickListener {
                    val intent = Intent(context, DetailJemaahAnimalActivity::class.java)
                    intent.putExtra(Constanta.ANIMAL_DATA, data)
                    intent.putExtra(Constanta.USER_DATA, masjidData)
                    context.startActivity(intent)
                }
            }

        }

        val falseAvailableList = listData.filter { !it.status && (it.jointVentureAmount - (it.idShohibulQurbaniList?.size ?: 0) > 0) }
        val falseList = listData.filter { !it.status && (it.jointVentureAmount - (it.idShohibulQurbaniList?.size ?: 0) == 0)}
        val trueList = listData.filter { it.status == true}

        val sortedList = ArrayList<Animal>().apply {
            addAll(falseAvailableList)
            addAll(falseList)
            addAll(trueList)
        }

        bind(sortedList[position])
    }


    override fun getItemCount() = listData.size

}
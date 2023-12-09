package com.androidexpert.qurbanku_apps_skripsi.adapter.transaction
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardQurbaniHistoryItemBinding
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper


class QurbaniHistoryAdapter(private val listData: ArrayList<TransactionDetail>) :
    RecyclerView.Adapter<QurbaniHistoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardQurbaniHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardQurbaniHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: TransactionDetail) {
            val transaction = data.transaction
            val masjid = data.masjid
            val jemaah = data.jemaah
            val animal = data.animal
            with(holder.itemView) {
                holder.binding.apply {
                    if (transaction != null && animal != null) {
                        with(animal) {
                            tvQurbaniDate.text = Helper.getLongSimpleDate(qurbaniTimeMillisecond)
                            val totalPrice = this.price + operationalCosts
                            val costRequired = totalPrice / jointVentureAmount
                            tvAnimalSpecies.text = speciesName
                            tvVarietyAnimal.text = varietyName
                            tvPrice.text = resources.getString(
                                R.string.price_2,
                                Helper.parseNumberFormat(costRequired)
                            )
                            tvWeightAnimal.text =
                                if (weight % 1 == 0.0) resources.getString(
                                    R.string.weight_value,
                                    String.format("%.0f kg", weight)
                                )
                                else resources.getString(
                                    R.string.weight_value,
                                    String.format("%.1f kg", weight)
                                )
                            tvCategory.text =
                                if (jointVentureAmount > 1) resources.getString(R.string.joint_venture)
                                else resources.getString(R.string.purchase_category)
                            val location = Helper.parseAddressCity(context, masjid!!.latitude!!, masjid.longitude!!)
                            tvQurbaniLocation.text = resources.getString(R.string.name_masjid_with_location, masjid.name, location)
                        }
                    }
                }

            }

        }
        val reversedList = listData.reversed()
        bind(reversedList[position])
    }


    override fun getItemCount() = listData.size

}
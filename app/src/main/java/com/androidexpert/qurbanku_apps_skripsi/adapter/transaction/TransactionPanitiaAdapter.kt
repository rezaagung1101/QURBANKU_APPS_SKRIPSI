package com.androidexpert.qurbanku_apps_skripsi.adapter.transaction

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardTransactionPanitiaItemBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.panitia.DetailTransactionPanitiaActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper


class TransactionPanitiaAdapter(private val listData: ArrayList<TransactionDetail>) :
    RecyclerView.Adapter<TransactionPanitiaAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardTransactionPanitiaItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardTransactionPanitiaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: TransactionDetail) {
            val transaction = data.transaction
            val jemaah = data.jemaah
            val animal = data.animal
            with(holder.itemView) {
                holder.binding.apply {
                    if (transaction != null && animal != null && jemaah != null) {
                        tvIdTransaction.text =
                            resources.getString(R.string.temp_id_transaction, transaction.id)
                        tvTransactionDate.text =
                            Helper.getLongSimpleDateTransaction(transaction.createdTimeMillisecond)

                            tvAnimalSpecies.text = resources.getString(R.string.species_variety, animal.speciesName, animal.varietyName)
                            if (transaction.status == true) {
                                cardContainer.setBackgroundColor(resources.getColor(R.color.green_sold_status))
                                tvIdTransaction.paintFlags = tvIdTransaction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            } else if (transaction.status == false) {
                                cardContainer.setBackgroundColor(resources.getColor(R.color.danger))
                                tvAnimalSpecies.setTextColor(resources.getColor(R.color.white))
                                tvJemaahName.setTextColor(resources.getColor(R.color.white))
                                tvIdTransaction.setTextColor(resources.getColor(R.color.white))
                                tvTransactionDate.setTextColor(resources.getColor(R.color.white))
                                tvIdTransaction.paintFlags = tvIdTransaction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            }
                            tvJemaahName.text = jemaah.name
                    }
                }
                this.setOnClickListener {
                    val intent = Intent(context, DetailTransactionPanitiaActivity::class.java)
                    intent.putExtra(Constanta.TRANSACTION_DATA, data)
                    context.startActivity(intent)
                }
            }

        }
        val unconfirmedTransaction = listData.filter { it.transaction!!.status == null }
        val confirmedTransaction = listData.filter { it.transaction!!.status != null }
        val sortedList = ArrayList<TransactionDetail>().apply{
            addAll(unconfirmedTransaction.sortedBy {it.transaction!!.createdTimeMillisecond})
            addAll(confirmedTransaction.sortedWith(compareByDescending{ it.transaction!!.createdTimeMillisecond }))
        }
        bind(sortedList[position])
    }


    override fun getItemCount() = listData.size

}
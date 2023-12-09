package com.androidexpert.qurbanku_apps_skripsi.adapter.transaction

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardTransactionJemaahItemBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.transaction.jemaah.DetailTransactionJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta
import com.androidexpert.qurbanku_apps_skripsi.utils.Helper


class TransactionJemaahAdapter(private val listData: ArrayList<TransactionDetail>) :
    RecyclerView.Adapter<TransactionJemaahAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardTransactionJemaahItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardTransactionJemaahItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: TransactionDetail) {
            val transaction = data.transaction
            val animal = data.animal
            with(holder.itemView) {
                holder.binding.apply {
                    if (transaction != null && animal != null) {
                        tvIdTransaction.text =
                            resources.getString(R.string.temp_id_transaction, transaction.id)
                        tvTransactionDate.text =
                            Helper.getLongSimpleDateTransaction(transaction.createdTimeMillisecond)
                        with(animal) {
                            val totalPrice = this.price + operationalCosts
                            val costRequired = totalPrice / jointVentureAmount
                            tvSpeciesName.text = speciesName
                            tvTotalTransfer.text = resources.getString(
                                R.string.price_2,
                                Helper.parseNumberFormat(costRequired)
                            )
                            if (transaction.status == true) {
                                tvTransactionStatus.text = resources.getString(R.string.accepted)
                                tvTransactionStatus.setTextColor(resources.getColor(R.color.green_main))
                                cardTransactionJemaah.strokeColor =
                                    resources.getColor(R.color.green_main)
                                cardContainer.setBackgroundColor(resources.getColor(R.color.green_sold_status))
                            } else if (transaction.status == false) {
                                tvTransactionStatus.text = resources.getString(R.string.rejected)
                                tvTransactionStatus.paintFlags = tvTransactionStatus.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                tvTransactionStatus.setTextColor(resources.getColor(R.color.danger))
                                cardTransactionJemaah.strokeColor =
                                    resources.getColor(R.color.danger)
                                cardContainer.setBackgroundColor(resources.getColor(R.color.disabled_background))
                            }
                        }
                    }
                }
                this.setOnClickListener {
                    val intent = Intent(context, DetailTransactionJemaahActivity::class.java)
                    intent.putExtra(Constanta.TRANSACTION_DATA, data)
                    context.startActivity(intent)
                }
            }

        }
        val reversedList = listData.reversed()
        bind(reversedList[position])
    }


    override fun getItemCount() = listData.size

}
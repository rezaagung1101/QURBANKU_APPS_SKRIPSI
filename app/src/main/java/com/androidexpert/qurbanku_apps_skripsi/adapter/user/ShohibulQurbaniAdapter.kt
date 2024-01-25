package com.androidexpert.qurbanku_apps_skripsi.adapter.animal

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.databinding.CardShohibulQurbaniItemBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.panitia.DetailProfileJemaahActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta

class ShohibulQurbaniAdapter(private val listData: ArrayList<User>, private val isAdmin: Boolean) :
    RecyclerView.Adapter<ShohibulQurbaniAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardShohibulQurbaniItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardShohibulQurbaniItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun bind(data: User) {
            with(holder.itemView) {
                holder.binding.apply {
                    tvShohibulQurbanName.text = data.name
                }
                this.setOnClickListener {
                    if (isAdmin) {
                        val intent = Intent(context, DetailProfileJemaahActivity::class.java)
                        intent.putExtra(Constanta.USER_DATA, data)
                        context.startActivity(intent)
                    }
                }
            }

        }
        bind(listData[position])
    }


    override fun getItemCount() = listData.size

}
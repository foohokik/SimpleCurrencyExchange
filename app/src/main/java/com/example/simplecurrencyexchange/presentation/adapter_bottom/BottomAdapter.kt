package com.example.simplecurrencyexchange.presentation.adapter_bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class BottomAdapter(private val listener: AdapterListener): ListAdapter<ValuteUI, BottomCurrencyViewHolder>(ValuteDiffUtilBottom()){


    //    RecyclerView.Adapter<BottomCurrencyViewHolder>() {
//
//    private val items = mutableListOf<ValuteUI>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomCurrencyViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return BottomCurrencyViewHolder(ItemCurrencyBinding.inflate(layoutInflater, parent, false))
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: BottomCurrencyViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    fun setItems(newItems: List<ValuteUI>) {
//        val diffResult = DiffUtil.calculateDiff(ValuteDiffUtilTop(items, newItems))
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomCurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
       return BottomCurrencyViewHolder(ItemCurrencyBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: BottomCurrencyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    override fun onBindViewHolder(holder: BottomCurrencyViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val item = getItem(position)
            holder.bindChangeResult(item)
        }
    }

}
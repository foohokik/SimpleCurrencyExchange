package com.example.simplecurrencyexchange.presentation.adapter_bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.adapter_top.TopCurrencyViewHolder
import com.example.simplecurrencyexchange.presentation.adapter_top.ValuteDiffUtil
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class BottomAdapter: ListAdapter<ValuteUI, BottomCurrencyViewHolder>(ValuteDiffUtil_()){


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
//        val diffResult = DiffUtil.calculateDiff(ValuteDiffUtil(items, newItems))
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
        holder.bind(item)
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
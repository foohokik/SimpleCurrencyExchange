package com.example.simplecurrencyexchange.presentation.adapter_top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class TopAdapter (private val listener: AdapterListener): ListAdapter<ValuteUI, TopCurrencyViewHolder>(ValuteDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TopCurrencyViewHolder(ItemCurrencyBinding.inflate(layoutInflater, parent, false))
    }

        override fun onBindViewHolder(holder: TopCurrencyViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item, listener)
    }

        override fun onBindViewHolder(holder: TopCurrencyViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val item = getItem(position)
            holder.bindChangeResult(item)
        }
    }



//    RecyclerView.Adapter<TopCurrencyViewHolder>() {
//
//    private val items = mutableListOf<ValuteUI>()
//    val _items = items
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCurrencyViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return TopCurrencyViewHolder(ItemCurrencyBinding.inflate(layoutInflater, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: TopCurrencyViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: TopCurrencyViewHolder, position: Int, payloads: List<Any>) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position)
//        } else {
//            holder.bindChangeResult(items[position])
//        }
//    }
//
//    fun setItems(newItems: List<ValuteUI>) {
//        val diffResult = DiffUtil.calculateDiff(ValuteDiffUtil(items, newItems))
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    fun setItem(newItem: ValuteUI, index: Int) {
//        items[index].copy(
//            convertationTopResult = newItem.convertationTopResult,
//            symbolAnotherCurrency = newItem.symbolAnotherCurrency
//        )
//        notifyItemChanged(index)
//    }

}
package com.example.simplecurrencyexchange.presentation.adapter_top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class TopAdapter (private val listener: AdapterListener): ListAdapter<ValuteUI, TopCurrencyViewHolder>(ValuteDiffUtilTop()) {

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

}
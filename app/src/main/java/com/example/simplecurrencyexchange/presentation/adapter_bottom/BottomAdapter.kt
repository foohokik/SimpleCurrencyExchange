package com.example.simplecurrencyexchange.presentation.adapter_bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class BottomAdapter(private val listener: AdapterListener) :
    ListAdapter<ValuteUI, BottomCurrencyViewHolder>(ValuteDiffUtilBottom()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomCurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BottomCurrencyViewHolder(ItemCurrencyBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: BottomCurrencyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    override fun onBindViewHolder(
        holder: BottomCurrencyViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val item = getItem(position)
            holder.bindChangeResult(item)
        }
    }

}
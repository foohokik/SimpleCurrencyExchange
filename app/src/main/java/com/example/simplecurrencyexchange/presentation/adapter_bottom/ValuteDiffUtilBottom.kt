package com.example.simplecurrencyexchange.presentation.adapter_bottom

import androidx.recyclerview.widget.DiffUtil
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class ValuteDiffUtilBottom : DiffUtil.ItemCallback<ValuteUI>() {

    override fun getChangePayload(oldItem: ValuteUI, newItem: ValuteUI): Any? {
        return if (oldItem != newItem) {
            true
        } else {
            null
        }
    }

    override fun areItemsTheSame(oldItem: ValuteUI, newItem: ValuteUI): Boolean {
        return ((oldItem.convertationResult == newItem.convertationResult)
                || (oldItem.convertationInputResult == newItem.convertationInputResult))
    }

    override fun areContentsTheSame(oldItem: ValuteUI, newItem: ValuteUI): Boolean {
        return oldItem == newItem
    }


}
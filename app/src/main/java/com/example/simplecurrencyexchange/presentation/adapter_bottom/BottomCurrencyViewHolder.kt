package com.example.simplecurrencyexchange.presentation.adapter_bottom

import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.R
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class BottomCurrencyViewHolder(val binding: ItemCurrencyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(valute: ValuteUI) =
        with(binding) {
            tvCharCode.text = valute.charCode
            tvBalance.text = root.context.getString(R.string.balance, valute.balance.toString(), valute.symbol)
            val roundedRes = "%.2f".format(valute.convertationResult)
            tvResult.text = root.context.getString(
                R.string.converter_main_text,
                valute.symbol,
                roundedRes,
                valute.symbolAnotherCurrency
            )
            val roundedResInput = "%.2f".format(valute.convertationInputResult)
            inputValue.setText(roundedResInput)
        }

    fun bindChangeResult(valute: ValuteUI) =
        with(binding) {
            val roundedRes = "%.2f".format(valute.convertationResult)
            tvResult.text = root.context.getString(
                R.string.converter_main_text,
                valute.symbol,
                roundedRes,
                valute.symbolAnotherCurrency
            )

            val roundedResInput = "%.2f".format(valute.convertationInputResult)
            inputValue.setText(roundedResInput)
        }
}


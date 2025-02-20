package com.example.simplecurrencyexchange.presentation.adapter_top

import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.R
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class TopCurrencyViewHolder(val binding: ItemCurrencyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(valute: ValuteUI, listener: AdapterListener) =
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
            inputValue.doOnTextChanged{ text, _, _, _ ->
                text?.let {
                    if (text.toString().isNotEmpty()) {
                        listener.onEditTextChange(text.toString())
                    }
                }
            }
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
        }

}


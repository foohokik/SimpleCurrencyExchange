package com.example.simplecurrencyexchange.presentation.adapter_bottom

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.R
import com.example.simplecurrencyexchange.databinding.ItemCurrencyBinding
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class BottomCurrencyViewHolder(val binding: ItemCurrencyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var isProgrammaticTextChange = false

    fun bind(valute: ValuteUI, listener: AdapterListener) =
        with(binding) {
            tvCharCode.text = valute.charCode
            val roundedbalance = "%.2f".format(valute.balance)
            tvBalance.text =
                root.context.getString(R.string.balance, roundedbalance, valute.symbol)
            val roundedRes = "%.2f".format(valute.convertationResult)
            tvResult.text = root.context.getString(
                R.string.converter_main_text,
                valute.symbol,
                roundedRes,
                valute.symbolAnotherCurrency
            )
            inputValue.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    val currentText = inputValue.text.toString()
                    if (currentText.isNotEmpty() || currentText == "") {
                        inputValue.setText("")
                    }
                }
            }
            inputValue.doOnTextChanged { text, _, _, _ ->
                if (!isProgrammaticTextChange) {
                    text?.let {
                        if (text.toString().isNotEmpty()) {
                            val newText = if (text.toString().contains(",")) {
                                text.toString().replace(',', '.')
                            } else {
                                text.toString()
                            }
                            listener.onEditTextChangeBottom(newText)
                        }
                    }
                }
            }

            isProgrammaticTextChange = true
            val roundedResInput = "%.2f".format(valute.convertationInputResultBottom)
            inputValue.setText(roundedResInput)
            isProgrammaticTextChange = false
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
            isProgrammaticTextChange = true
            val roundedResInput = "%.2f".format(valute.convertationInputResultBottom)
            inputValue.setText(roundedResInput)
            isProgrammaticTextChange = false
            val roundedbalance = "%.2f".format(valute.balance)
            tvBalance.text =
                root.context.getString(R.string.balance, roundedbalance, valute.symbol)
        }

}


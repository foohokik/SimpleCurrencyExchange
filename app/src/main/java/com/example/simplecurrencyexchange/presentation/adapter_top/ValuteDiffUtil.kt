package com.example.simplecurrencyexchange.presentation.adapter_top

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.simplecurrencyexchange.presentation.model.ValuteUI

class ValuteDiffUtil: DiffUtil.ItemCallback<ValuteUI>() {


    //    (
//    private val oldList: List<ValuteUI>,
//    private val newList: List<ValuteUI>
//) : DiffUtil.Callback() {
//
//    override fun getOldListSize(): Int = oldList.size
//
//    override fun getNewListSize(): Int = newList.size
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldItem = oldList[oldItemPosition]
//        val newItem = newList[newItemPosition]
//        return oldItem.convertationTopResult == newItem.convertationTopResult
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        Log.d("MyTag", "areContentsTheSame  =  "
//                + (oldList[oldItemPosition] == newList[newItemPosition]).toString())
//        return oldList[oldItemPosition] == newList[newItemPosition]
//    }
//
//    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
//        return if (oldItemPosition != newItemPosition) {
//            true
//        } else {
//            null
//        }
//    }

    override fun getChangePayload(oldItem: ValuteUI, newItem: ValuteUI): Any? {
        return if (oldItem != newItem) {
            true
        } else {
            null
        }
    }

    override fun areItemsTheSame(oldItem: ValuteUI, newItem: ValuteUI): Boolean {
        return oldItem.convertationResult == newItem.convertationResult
    }

    override fun areContentsTheSame(oldItem: ValuteUI, newItem: ValuteUI): Boolean {
        return oldItem == newItem
    }


}
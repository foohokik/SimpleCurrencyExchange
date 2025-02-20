package com.example.simplecurrencyexchange.core.extension

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import java.io.Serializable



fun Context.hideKeyboard(view: View) {
        val inputMethodManager =  ContextCompat.
            getSystemService(view.context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyBoard(view: View?) {
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            imm.showSoftInput(view, 0)
        }
    }

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

 inline fun <reified VM : ViewModel> Fragment.viewModelFactory(
    noinline factory: () -> VM,
): Lazy<VM> = viewModels {
    ViewModelFactory(factory)
}

 inline fun <reified VM : ViewModel> AppCompatActivity.viewModelFactory(
    noinline factory: () -> VM,
): Lazy<VM> = viewModels {
    ViewModelFactory(factory)
}

 class ViewModelFactory<VM : ViewModel>(
    private val factory: () -> VM,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T
    = factory() as T
}

fun Double?.safeDiv(other: Double?): Double? {
    return if (this != null && other != null && other != 0.0) {
        this / other
    } else {
        null
    }
}
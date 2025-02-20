package com.example.simplecurrencyexchange.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplecurrencyexchange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        savedInstanceState ?: initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                binding.mainContainerView.id,
                ConverterFragment(),
                PRODUCTS_FRAGMENT_TAG
            )
            .commit()
    }

    companion object{
        const val PRODUCTS_FRAGMENT_TAG = "CONVERTER_FRAGMENT_TAG"
    }

}
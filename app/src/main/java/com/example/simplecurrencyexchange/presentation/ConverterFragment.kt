package com.example.simplecurrencyexchange.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyexchange.App
import com.example.simplecurrencyexchange.R
import com.example.simplecurrencyexchange.core.extension.viewModelFactory
import com.example.simplecurrencyexchange.databinding.FragmentConverterBinding
import com.example.simplecurrencyexchange.presentation.adapter_bottom.BottomAdapter
import com.example.simplecurrencyexchange.presentation.adapter_top.TopAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ConverterFragment : Fragment() {

    @Inject
    internal lateinit var viewModelProvider: Provider<ConverterViewModel>

    private var _binding: FragmentConverterBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private lateinit var topAdapter: TopAdapter
    private lateinit var bottomAdapter: BottomAdapter


    private val viewModel by viewModelFactory { viewModelProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTopRecycleView()
        initBottomRecycleView()
        observe()
        onExchangeClick()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect { topAdapter.submitList(it.itemsTopValute) }
                }
                launch {
                    viewModel.stateFlow.collect {
                        bottomAdapter.submitList(it.itemsBottomValute)
                    }
                }
                launch { viewModel.stateFlow.collect { onToolBarTextChange(it) } }
                launch { viewModel.sideEffects.collect(::handleSideEffects) }
            }
        }
    }

    private fun initTopRecycleView() {
        val snapHelper = PagerSnapHelper()
        with(binding.rvTop) {
            topAdapter = TopAdapter(viewModel)
            val manager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = manager
            adapter = topAdapter
            snapHelper.attachToRecyclerView(this)
            itemAnimator = null
            addOnScrollListener(scrollListenerTop)
        }
    }

    private fun initBottomRecycleView() {
        val snapHelper = PagerSnapHelper()
        with(binding.rvBottom) {
            bottomAdapter = BottomAdapter(viewModel)
            val manager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = manager
            adapter = bottomAdapter
            itemAnimator = null
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(scrollListenerBottom)
        }
    }


    private val scrollListenerTop = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.rvTop.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            viewModel.topUpdateFlow(firstVisibleItemPosition)
        }

    }

    private val scrollListenerBottom = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.rvBottom.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            viewModel.bottomUpdateFlow(firstVisibleItemPosition)
        }
    }


    private fun onToolBarTextChange(state: ConverterState) {
        val roundedRes = "%.2f".format(state.resultTop)
        binding.tvResultToolbar.text = context?.getString(
            R.string.converter_main_text,
            state.symbol,
            roundedRes,
            state.symbolAnotherCurrency
        )
    }

    private fun onExchangeClick() {
        binding.exchange.setOnClickListener {
            viewModel.doExchange()
        }
    }

    private fun getBalances(): String {
        val listValute = viewModel.stateFlow.value.itemsTopValute
        val stringBuilder = StringBuilder()

        for (valute in listValute) {
            val balance = "%.2f".format(valute.balance)
            stringBuilder.append("${valute.charCode} : ${balance} ${valute.symbol}\n")
        }
        return stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvTop.removeOnScrollListener(scrollListenerTop)
        binding.rvBottom.removeOnScrollListener(scrollListenerBottom)
        _binding = null
    }

    private fun showExchangeTransactionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.approve)
            .setMessage(getBalances())
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.CautionEffect -> Toast.makeText(
                requireContext(),
                getString(R.string.caution),
                Toast.LENGTH_LONG
            ).show()

            is SideEffects.ErrorEffect -> Toast.makeText(
                requireContext(),
                getString(R.string.error, sideEffects.err),
                Toast.LENGTH_LONG
            ).show()

            is SideEffects.ExceptionEffect -> Toast.makeText(
                requireContext(),
                getString(R.string.error, sideEffects.throwable),
                Toast.LENGTH_LONG
            ).show()

            is SideEffects.onClickExchange -> showExchangeTransactionDialog()
        }
    }

}
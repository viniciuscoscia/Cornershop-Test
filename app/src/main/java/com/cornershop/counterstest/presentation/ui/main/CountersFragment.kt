package com.cornershop.counterstest.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainBinding
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.*
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show
import com.cornershop.counterstest.presentation.commons.util.showGenericErrorDialog
import com.cornershop.counterstest.presentation.model.CounterUiModel
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountersFragment : Fragment(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModel()
    private val viewBinding: FragmentMainBinding by viewBinding() //Using lib to avoid ViewBinding Memory Leak
    private var countersAdapter: CountersAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        viewModel.countersLiveData.observe(viewLifecycleOwner) { counterUiModel ->
            when (counterUiModel) {
                is ViewState.Success -> {
                    hideLoading()
                    setupCountersLayoutInfo(counterUiModel.value)
                }
                is ViewState.Error -> {
                    hideLoading()
                    checkErrorEvent(counterUiModel.errorEvent)
                }
                is ViewState.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun hideLoading() {
        viewBinding.loading.root.hide()
    }

    private fun showLoading() {
        viewBinding.loading.root.show()
    }

    private fun checkErrorEvent(errorEvent: ErrorEvent) {
        when (errorEvent) {
            is CountersErrorEvents.IncreaseCounterNetworkUnavailable -> {
                errorEvent.showIncreaseDecreaseErrorDialog {
                    viewModel.onIncreaseCounter(errorEvent.counterUiModel)
                }
            }
            is CountersErrorEvents.DecreaseCounterNetworkUnavailable -> {
                errorEvent.showIncreaseDecreaseErrorDialog {
                    viewModel.onDecreaseCounter(errorEvent.counterUiModel)
                }
            }
            is DialogErrorEvent -> {
                context?.run {
                    errorEvent.showErrorDialog(this)
                }
            }
            else -> {
                context?.showGenericErrorDialog()
            }
        }
    }

    private fun setupCountersLayoutInfo(uiModel: CountersFragmentUiModel) {
        val counters = uiModel.counters

        if (counters.isEmpty()) {
            onNoCountersOrError()
            return
        }

        with(viewBinding) {
            itemsCounter.text = getString(R.string.n_items, uiModel.itemsCount)
            timesCounter.text = getString(R.string.n_times, uiModel.timesCount)
        }

        checkAdapter(counters)
    }

    private fun checkAdapter(counters: List<CounterUiModel>) {
        if (countersAdapter == null) {
            setupAdapterOnRecyclerView(counters)
        } else {
            countersAdapter!!.updateCounters(counters)
        }
    }

    private fun setupAdapterOnRecyclerView(counters: List<CounterUiModel>) {
        countersAdapter = CountersAdapter(counters,
            onIncreaseCounterClicked = { counterUiModel ->
                viewModel.onIncreaseCounter(counterUiModel)
            }, onDecrementCounterClicked = { counterUiModel ->
                viewModel.onDecreaseCounter(counterUiModel)
            })

        viewBinding.countersRecyclerView.adapter = countersAdapter
    }

    private fun onNoCountersOrError() = with(viewBinding) {
        itemsCounter.hide()
        timesCounter.hide()
        countersRecyclerView.hide()
        noCountersLayout.root.show()
    }

    private fun IncreaseDecreaseCounterErrorEvent.showIncreaseDecreaseErrorDialog(
        onRetryClicked: () -> Unit
    ) {
        val context = context ?: return

        with(AlertDialog.Builder(context)) {
            setTitle(
                context.getString(
                    errorTitle,
                    counterUiModel.title,
                    getCalculatedCounterValue()
                )
            )
            setMessage(
                context.getString(errorMessage)
            )
            setPositiveButton(positiveButton) { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton(negativeButton) { dialog, _ ->
                onRetryClicked()
                dialog.dismiss()
            }
        }
    }
}
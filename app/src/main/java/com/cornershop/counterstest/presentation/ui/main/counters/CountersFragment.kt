package com.cornershop.counterstest.presentation.ui.main.counters

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.*
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show
import com.cornershop.counterstest.presentation.commons.util.showGenericErrorDialog
import com.cornershop.counterstest.presentation.model.CounterUiModel
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountersFragment : Fragment(R.layout.fragment_counters) {
    private val viewModel: CountersViewModel by viewModel()
    private val viewBinding: FragmentCountersBinding by viewBinding()
    private var countersAdapter: CountersAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        setupListeners()
        setupLiveData()
    }

    private fun setupListeners() = with(viewBinding) {
        addCounterButton.setOnClickListener {

        }
        couldNotLoadCountersLayout.retry.setOnClickListener {
            viewModel.getCounters()
        }
    }

    private fun setupLiveData() {
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

    private fun checkErrorEvent(errorEvent: ErrorEvent) {
        when (errorEvent) {
            is CountersErrorEvents.GetCountersNetworkUnavailable -> {
                showCouldNotGetCountersError()
            }
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
            is CommonErrorEvents.Generic -> {
                context?.showGenericErrorDialog()
            }
        }
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

    private fun showCouldNotGetCountersError() = with(viewBinding) {
        showCountersLayout(show = false)
        noCountersLayout.root.hide()

        couldNotLoadCountersLayout.root.show()
    }

    private fun setupCountersLayoutInfo(uiModel: CountersFragmentUiModel) {
        val counters = uiModel.counters

        if (counters.isEmpty()) {
            onNoCountersOrError()
            return
        }

        with(viewBinding) {
            showCountersLayout(show = true)
            couldNotLoadCountersLayout.root.hide()
            noCountersLayout.root.hide()


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
        showCountersLayout(show = false)
        couldNotLoadCountersLayout.root.hide()

        noCountersLayout.root.show()
    }

    private fun showCountersLayout(show: Boolean) = with(viewBinding) {
        val counterViews = listOf(itemsCounter, timesCounter, countersRecyclerView)

        counterViews.forEach { view -> if (show) view.show() else view.hide() }
    }

    private fun hideLoading() {
        viewBinding.loading.root.hide()
    }

    private fun showLoading() {
        viewBinding.loading.root.show()
    }
}
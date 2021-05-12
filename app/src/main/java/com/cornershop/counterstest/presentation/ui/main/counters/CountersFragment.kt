package com.cornershop.counterstest.presentation.ui.main.counters

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.presentation.commons.errorevent.*
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.invisible
import com.cornershop.counterstest.presentation.commons.util.show
import com.cornershop.counterstest.presentation.commons.util.showGenericErrorDialog
import com.cornershop.counterstest.presentation.commons.viewstate.MultiSelectionState
import com.cornershop.counterstest.presentation.commons.viewstate.ViewState
import com.cornershop.counterstest.presentation.customsearchview.CustomSearchViewState
import com.cornershop.counterstest.presentation.model.CounterUiModel
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountersFragment : Fragment(R.layout.fragment_counters) {
	private val viewModel: CountersViewModel by viewModel()
	private val viewBinding: FragmentCountersBinding by viewBinding()
	private var tracker: SelectionTracker<CounterUiModel>? = null

	private var isSearching: Boolean = false

	private val countersAdapter: CountersAdapter by lazy {
		CountersAdapter(
			onIncreaseCounterClicked = { counterUiModel ->
				viewModel.onIncreaseCounter(counterUiModel)
			},
			onDecrementCounterClicked = { counterUiModel ->
				viewModel.onDecreaseCounter(counterUiModel)
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		lifecycle.addObserver(viewModel)

		setupListeners()
		setupLiveData()
		setupSwipeRefreshLayout()
		setupSearchBar()
		setupMultiSelectionBar()
	}

	private fun setupMultiSelectionBar() = with(viewBinding.multiSelectionBar) {
		deleteCounterButton.setOnClickListener {
			onDeleteCounter()
		}

		shareCounterButton.setOnClickListener {
			tracker?.run {
				if (hasSelection()) {
					shareCounter(selection.first())
				}
			}
		}

		closeBarButton.setOnClickListener {
			exitMultiSelectionMode()
		}
	}

	private fun onDeleteCounter() {
		viewBinding.searchView.show()
		viewBinding.multiSelectionBar.root.hide()

		viewModel.onDeleteCounter()
	}

	private fun setupSearchBar() {
		viewBinding.searchView.initView(
			onTextChangedListener = { viewState ->
				when (viewState) {
					is CustomSearchViewState.Writing -> {
						hideDarkEffect()
						viewModel.searchCountersByText(viewState.text)
					}
					is CustomSearchViewState.EmptyText -> {
						showDarkEffect()
						viewModel.getCounters()
					}
				}
			},
			onExitSearchModeListener = {
				isSearching = false
				hideDarkEffect()
				viewModel.getCounters(false)
			}, onSearchViewClick = {
				isSearching = true
				showDarkEffect()
			}
		)
	}

	override fun onResume() {
		super.onResume()
		viewModel.getCounters()
	}

	private fun setupSwipeRefreshLayout() = with(viewBinding.countersSwipeRefreshLayout) {
		setOnRefreshListener {
			viewModel.getCounters(isSwipeRefresh = true)
		}
		setColorSchemeResources(R.color.orange)
	}

	private fun setupListeners() = with(viewBinding) {
		addCounterButton.setOnClickListener {
			navigateToAddCounter()
		}

		couldNotLoadCountersLayout.retry.setOnClickListener {
			couldNotLoadCountersLayout.root.hide()
			viewModel.getCounters()
		}

		darkEffectView.setOnClickListener {
			searchView.exitSearchMode()
		}
	}

	private fun shareCounter(counter: CounterUiModel) {
		val sendIntent: Intent = Intent().apply {
			action = Intent.ACTION_SEND
			putExtra(
				Intent.EXTRA_TEXT,
				getString(R.string.share_text, counter.count, counter.title)
			)
			type = COUNTER_SHARE_TYPE
		}

		startActivity(Intent.createChooser(sendIntent, null))
	}

	private fun navigateToAddCounter() {
		findNavController()
			.navigate(
				CountersFragmentDirections.actionCountersFragmentToCreateCounterFragment(null)
			)
	}

	private fun setupLiveData() = with(viewModel) {
		countersLiveData.observe(viewLifecycleOwner) { counterUiModel ->
			if ((counterUiModel is ViewState.Loading).not()) hideLoading()

			when (counterUiModel) {
				is ViewState.Loading -> {
					showLoading()
				}
				is ViewState.Success -> {
					setupCountersLayoutInfo(counterUiModel.value)
				}
				is ViewState.Error -> {
					checkErrorEvent(counterUiModel.errorEvent)
				}
				is ViewState.SuccessEmpty -> {
					onNoCountersOrError()
				}
			}
		}

		multiSelectionLiveData.observe(viewLifecycleOwner) { state ->
			when (state) {
				is MultiSelectionState.Enabled -> {
					countersAdapter.enterSelectionMode()
				}
				is MultiSelectionState.Disabled -> {
					unselectCounters()
					countersAdapter.exitSelectionMode()
				}
			}
		}
	}

	private fun unselectCounters() {
		tracker?.run {
			if (selection.isEmpty.not()) {
				setItemsSelected(selection.toMutableList(), false)
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

	private fun showCouldNotGetCountersError() = with(viewBinding) {
		showCountersLayout(show = false)
		noCountersLayout.root.hide()
		noResultsText.hide()
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
			noResultsText.hide()

			itemsCounter.text = getString(R.string.n_items, uiModel.itemsCount)
			timesCounter.text = getString(R.string.n_times, uiModel.timesCount)
		}

		setRecyclerviewItems(counters)
	}

	private fun setRecyclerviewItems(counters: List<CounterUiModel>) = with(viewBinding) {
		if (countersRecyclerView.adapter == null) {
			viewBinding.countersRecyclerView.adapter = countersAdapter
			setupTracker()
		}
		countersAdapter.setCounters(counters)
	}

	private fun setupTracker() {
		tracker = getSelectionTracker()
		setupTrackObserver()

		countersAdapter.tracker = tracker
	}

	private fun setupTrackObserver() {
		tracker?.addObserver(trackerObserver)
	}

	private val trackerObserver = object : SelectionTracker.SelectionObserver<CounterUiModel>() {
		override fun onSelectionChanged() {
			super.onSelectionChanged()
			tracker?.run {
				if (hasSelection()) {
					enterMultiSelectionMode(selection.firstOrNull())
				} else {
					exitMultiSelectionMode()
				}
			}
		}
	}

	fun exitMultiSelectionMode() {
		viewBinding.searchView.show()
		viewBinding.multiSelectionBar.root.hide()

		viewModel.exitMultiSelectionMode()
	}

	fun enterMultiSelectionMode(counter: CounterUiModel?) {
		viewBinding.searchView.invisible()
		viewBinding.multiSelectionBar.root.show()

		viewModel.enterMultiSelectionMode(counter?.id ?: "")
	}

	private fun getSelectionTracker() = SelectionTracker.Builder(
		COUNTERS_SELECTION_ID,
		viewBinding.countersRecyclerView,
		CounterItemKeyProvider(countersAdapter),
		CounterItemLookup(viewBinding.countersRecyclerView),
		StorageStrategy.createParcelableStorage(CounterUiModel::class.java)
	).withSelectionPredicate(
		SelectionPredicates.createSelectSingleAnything()
	).build()

	private fun onNoCountersOrError() = with(viewBinding) {
		showCountersLayout(show = false)
		couldNotLoadCountersLayout.root.hide()

		if (isSearching) {
			noResultsText.show()
			noCountersLayout.root.hide()
		} else {
			noResultsText.hide()
			noCountersLayout.root.show()
		}
	}

	private fun showCountersLayout(show: Boolean) = with(viewBinding) {
		val counterViews = listOf(itemsCounter, timesCounter, countersRecyclerView)

		counterViews.forEach { view -> if (show) view.show() else view.hide() }
	}

	private fun hideLoading() = with(viewBinding) {
		countersSwipeRefreshLayout.isRefreshing = false

		loading.root.hide()
	}

	private fun showLoading() {
		viewBinding.loading.root.show()
	}

	private fun showDarkEffect() = with(viewBinding.darkEffectView) {
		if (visibility != View.VISIBLE) show()
	}

	private fun hideDarkEffect() = with(viewBinding.darkEffectView) {
		hide()
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
			show()
		}
	}

	companion object {
		private const val COUNTERS_SELECTION_ID = "COUNTERS_SELECTION_ID"
		private const val COUNTER_SHARE_TYPE = "text/plain"
	}
}
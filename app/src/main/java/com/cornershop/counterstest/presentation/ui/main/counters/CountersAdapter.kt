package com.cornershop.counterstest.presentation.ui.main.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersLayoutBinding
import com.cornershop.counterstest.presentation.model.CounterUiModel

class CountersAdapter(
	private val onIncreaseCounterClicked: (counter: CounterUiModel) -> Unit,
	private val onDecrementCounterClicked: (counter: CounterUiModel) -> Unit
) : RecyclerView.Adapter<CountersAdapter.ViewHolder>() {

	init {
		setHasStableIds(true)
	}

	private var counters: List<CounterUiModel> = emptyList()
	fun setCounters(counters: List<CounterUiModel>) {
		this.counters = counters
		notifyDataSetChanged()
	}

	var tracker: SelectionTracker<CounterUiModel>? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val viewBinding =
			CountersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(viewBinding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if (tracker == null) return

		val isSelected = tracker!!.isSelected(counters[position])
		val counter = counters[position]

		with(holder) {
			viewBinding.counterTitleTextView.text = counter.title
			viewBinding.counterQuantityTextView.text = counter.count.toString()

			viewBinding.setupDecrementCounterButton(holder, counter)

			viewBinding.increaseCounterButton.setOnClickListener {
				onIncreaseCounterClicked(counter)
			}

			viewBinding.decrementCounterButton.setOnClickListener {
				onDecrementCounterClicked(counter)
			}

			itemView.isActivated = isSelected
		}
	}

	private fun CountersLayoutBinding.setupDecrementCounterButton(
		holder: ViewHolder,
		counter: CounterUiModel
	) {
		decrementCounterButton.setImageDrawable(
			ContextCompat.getDrawable(
				holder.itemView.context,
				if (counter.count < 1) {
					decrementCounterButton.isEnabled = false
					R.drawable.ic_disabled_minus
				} else {
					decrementCounterButton.isEnabled = true
					R.drawable.ic_minus_orange
				}
			)
		)
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount() = counters.size

	fun getItem(position: Int) = counters[position]
	fun getPosition(id: String) = counters.indexOfFirst { it.id == id }

	inner class ViewHolder(
		val viewBinding: CountersLayoutBinding
	) : RecyclerView.ViewHolder(viewBinding.root) {
		fun getItemDetails(): ItemDetailsLookup.ItemDetails<CounterUiModel> {
			return object : ItemDetailsLookup.ItemDetails<CounterUiModel>() {
				override fun getPosition() = absoluteAdapterPosition

				override fun getSelectionKey() = counters[position]
			}
		}
	}
}
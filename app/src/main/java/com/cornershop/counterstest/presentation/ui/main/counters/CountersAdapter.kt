package com.cornershop.counterstest.presentation.ui.main.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersLayoutBinding
import com.cornershop.counterstest.presentation.model.CounterUiModel

class CountersAdapter(
	private val onIncreaseCounterClicked: (counter: CounterUiModel) -> Unit,
	private val onDecrementCounterClicked: (counter: CounterUiModel) -> Unit,
	private val onItemClickListener: (counter: CounterUiModel) -> Unit,
	private val onLongClickListener: (counter: CounterUiModel) -> Unit
) : RecyclerView.Adapter<CountersAdapter.ViewHolder>() {

	private var counters: List<CounterUiModel> = emptyList()
	fun setCounters(counters: List<CounterUiModel>) {
		this.counters = counters
		notifyDataSetChanged()
	}

	var isMultiSelecting: Boolean = false
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val viewBinding =
			CountersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(viewBinding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val counter = counters[position]
		with(holder.viewBinding) {
			counterTitleTextView.text = counter.title
			counterQuantityTextView.text = counter.count.toString()

			setupDecrementCounterButton(holder, counter)

			increaseCounterButton.setOnClickListener {
				onIncreaseCounterClicked(counter)
			}

			decrementCounterButton.setOnClickListener {
				onDecrementCounterClicked(counter)
			}
		}

		holder.itemView.setOnClickListener {
			onItemClickListener(counter)
		}

		holder.itemView.setOnLongClickListener {
			onLongClickListener(counter)
			true
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

	override fun getItemCount() = counters.size

	inner class ViewHolder(
		val viewBinding: CountersLayoutBinding
	) : RecyclerView.ViewHolder(viewBinding.root)
}
package com.cornershop.counterstest.presentation.ui.main.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.CountersLayoutBinding
import com.cornershop.counterstest.presentation.model.CounterUiModel

class CountersAdapter(
    private var counters: List<CounterUiModel>,
    private val onIncreaseCounterClicked: (counterId: CounterUiModel) -> Unit,
    private val onDecrementCounterClicked: (counter: CounterUiModel) -> Unit
) : RecyclerView.Adapter<CountersAdapter.ViewHolder>() {

    fun updateCounters(counters: List<CounterUiModel>) {
        this.counters = counters
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
            increaseCounterButton.setOnClickListener {
                onIncreaseCounterClicked(counter)
            }
            decrementCounterButton.setOnClickListener {
                onDecrementCounterClicked(counter)
            }
        }
    }

    override fun getItemCount() = counters.size

    inner class ViewHolder(
        val viewBinding: CountersLayoutBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)
}
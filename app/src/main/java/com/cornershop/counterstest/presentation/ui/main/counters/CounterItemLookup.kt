package com.cornershop.counterstest.presentation.ui.main.counters

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.presentation.model.CounterUiModel

class CounterItemLookup(
	private val recyclerView: RecyclerView
) : ItemDetailsLookup<CounterUiModel>() {
	override fun getItemDetails(event: MotionEvent): ItemDetails<CounterUiModel>? {
		val view = recyclerView.findChildViewUnder(event.x, event.y)
		if (view != null) {
			return (recyclerView.getChildViewHolder(view) as CountersAdapter.ViewHolder)
				.getItemDetails()
		}
		return null
	}
}
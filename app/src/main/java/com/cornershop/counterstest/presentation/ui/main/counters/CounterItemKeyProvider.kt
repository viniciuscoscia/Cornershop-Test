package com.cornershop.counterstest.presentation.ui.main.counters

import androidx.recyclerview.selection.ItemKeyProvider
import com.cornershop.counterstest.presentation.model.CounterUiModel

class CounterItemKeyProvider(
	private val adapter: CountersAdapter
) : ItemKeyProvider<CounterUiModel>(SCOPE_CACHED) {
	override fun getKey(position: Int): CounterUiModel {
		return adapter.getItem(position)
	}

	override fun getPosition(key: CounterUiModel): Int {
		return adapter.getPosition(key.id)
	}
}
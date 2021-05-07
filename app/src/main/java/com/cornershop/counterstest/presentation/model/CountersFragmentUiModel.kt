package com.cornershop.counterstest.presentation.model

data class CountersFragmentUiModel(
    val itemsCount: Int,
    val timesCount: Int,
    val counters: List<CounterUiModel>,
)
package com.cornershop.counterstest.presentation.customsearchview

sealed interface CustomSearchViewState {
	data class Writing(val text: String) : CustomSearchViewState
	object EmptyText : CustomSearchViewState
}

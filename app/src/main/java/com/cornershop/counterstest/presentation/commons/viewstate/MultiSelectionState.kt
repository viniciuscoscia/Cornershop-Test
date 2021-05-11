package com.cornershop.counterstest.presentation.commons.viewstate

sealed interface MultiSelectionState {
	object Enabled : MultiSelectionState
	object Disabled : MultiSelectionState
}
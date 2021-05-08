package com.cornershop.counterstest.presentation.commons

import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent

sealed interface ViewState<out T> {
    object Loading : ViewState<Nothing>
    object SuccessEmpty : ViewState<Nothing>
    data class Success<out T>(val value: T) : ViewState<T>
    data class Error(val errorEvent: ErrorEvent) : ViewState<Nothing>
}

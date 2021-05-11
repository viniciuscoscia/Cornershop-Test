package com.cornershop.counterstest.presentation.commons.viewstate

import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent

sealed interface ViewState<out T> {
    sealed interface Loading : ViewState<Nothing> {
        object SwipeRefresh : Loading
        object Normal : Loading
    }

    object SuccessEmpty : ViewState<Nothing>
    data class Success<out T>(val value: T) : ViewState<T>
    data class Error(val errorEvent: ErrorEvent) : ViewState<Nothing>
}
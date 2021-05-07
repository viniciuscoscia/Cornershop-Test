package com.cornershop.counterstest.presentation.commons.errorevent

import androidx.annotation.StringRes
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.model.CounterUiModel

sealed interface CountersErrorEvents : ErrorEvent {
    object GetCountersNetworkUnavailable : CountersErrorEvents

    data class IncreaseCounterNetworkUnavailable(
        override val counterUiModel: CounterUiModel
    ) : IncreaseDecreaseCounterErrorEvent(counterUiModel) {
        @StringRes
        override val errorTitle: Int = R.string.error_updating_counter_title
        @StringRes
        override val errorMessage: Int = R.string.connection_error_description

        override fun getCalculatedCounterValue(): Int = counterUiModel.count + 1
    }

    data class DecreaseCounterNetworkUnavailable(
        override val counterUiModel: CounterUiModel
    ) : IncreaseDecreaseCounterErrorEvent(counterUiModel) {
        @StringRes
        override val errorTitle: Int = R.string.error_updating_counter_title
        @StringRes
        override val errorMessage: Int = R.string.connection_error_description

        override fun getCalculatedCounterValue(): Int = counterUiModel.count - 1
    }

    object DeleteCounterNetworkUnavailable : DialogErrorEvent {
        @StringRes
        override val errorTitle: Int = R.string.error_deleting_counter_title
        @StringRes
        override val errorMessage: Int = R.string.connection_error_description
    }
}

sealed class IncreaseDecreaseCounterErrorEvent(open val counterUiModel: CounterUiModel) :
    DialogErrorEvent {
    abstract override val errorTitle: Int
    val positiveButton: Int = R.string.dismiss
    val negativeButton: Int = R.string.retry

    abstract fun getCalculatedCounterValue(): Int
}
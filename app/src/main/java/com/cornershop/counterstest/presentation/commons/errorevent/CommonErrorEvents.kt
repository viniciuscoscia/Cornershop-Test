package com.cornershop.counterstest.presentation.commons.errorevent

import androidx.annotation.StringRes
import com.cornershop.counterstest.R

sealed interface CommonErrorEvents : ErrorEvent {
    object Generic : DialogErrorEvent {
        @StringRes
        override val errorTitle: Int? = null
        @StringRes
        override val errorMessage: Int = R.string.generic_error_description
    }

    object NetworkUnavailable : DialogErrorEvent {
        @StringRes
        override val errorTitle: Int? = null
        @StringRes
        override val errorMessage: Int = R.string.connection_error_description
    }
}
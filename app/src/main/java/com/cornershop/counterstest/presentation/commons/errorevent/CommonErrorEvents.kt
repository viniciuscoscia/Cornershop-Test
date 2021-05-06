package com.cornershop.counterstest.presentation.commons.errorevent

import androidx.annotation.StringRes
import com.cornershop.counterstest.R

enum class CommonErrorEvents(@StringRes val resourceId: Int) : ErrorEvent {
    GENERIC(R.string.connection_error_description),
    NETWORK_UNAVAILABLE(R.string.connection_error_description);

    override fun getErrorResource() = resourceId
}
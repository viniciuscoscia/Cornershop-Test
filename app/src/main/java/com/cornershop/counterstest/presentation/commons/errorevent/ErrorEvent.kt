package com.cornershop.counterstest.presentation.commons.errorevent

import androidx.annotation.StringRes

interface ErrorEvent {
    @StringRes
    fun getErrorResource(): Int
}
package com.cornershop.counterstest.data.helper

sealed interface ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>
    sealed interface NetworkErrorEntity : ResultWrapper<Nothing> {
        object Timeout : NetworkErrorEntity
        object NoInternetConnection : NetworkErrorEntity
        object NotFound : NetworkErrorEntity
        object AccessDenied : NetworkErrorEntity
        object ServiceUnavailable : NetworkErrorEntity
        object Unknown : NetworkErrorEntity
        object UnknownHttpException : NetworkErrorEntity
    }
}
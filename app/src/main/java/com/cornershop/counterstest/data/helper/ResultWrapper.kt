package com.cornershop.counterstest.data.helper

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    sealed class ErrorEntity : ResultWrapper<Nothing>() {
        object Network : ErrorEntity()
        object NotFound : ErrorEntity()
        object AccessDenied : ErrorEntity()
        object ServiceUnavailable : ErrorEntity()
        object Unknown : ErrorEntity()
        object UnknownHttpException : ErrorEntity()
    }
}
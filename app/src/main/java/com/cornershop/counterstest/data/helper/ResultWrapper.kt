package com.cornershop.counterstest.data.helper

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val errorEntity: ErrorEntity) : ResultWrapper<Nothing>()
}
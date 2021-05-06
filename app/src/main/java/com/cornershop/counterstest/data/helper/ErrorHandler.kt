package com.cornershop.counterstest.data.helper

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}

object GeneralErrorHandlerImpl : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ErrorEntity.Network

                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_UNAVAILABLE,
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ServiceUnavailable

                    else -> ErrorEntity.UnknownHttpException
                }
            }
            else -> ErrorEntity.Unknown
        }
    }
}
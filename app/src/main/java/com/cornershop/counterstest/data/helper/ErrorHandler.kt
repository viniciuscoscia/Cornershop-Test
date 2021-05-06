package com.cornershop.counterstest.data.helper

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

interface ErrorHandler {
    fun getError(throwable: Throwable): ResultWrapper.ErrorEntity
}

object RemoteDataErrorHandlerImpl : ErrorHandler {
    override fun getError(throwable: Throwable): ResultWrapper.ErrorEntity {
        return when (throwable) {
            is IOException -> ResultWrapper.ErrorEntity.Network
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ResultWrapper.ErrorEntity.Network

                    HttpURLConnection.HTTP_NOT_FOUND -> ResultWrapper.ErrorEntity.NotFound

                    HttpURLConnection.HTTP_FORBIDDEN -> ResultWrapper.ErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_UNAVAILABLE,
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ResultWrapper.ErrorEntity.ServiceUnavailable

                    else -> ResultWrapper.ErrorEntity.UnknownHttpException
                }
            }
            else -> ResultWrapper.ErrorEntity.Unknown
        }
    }
}
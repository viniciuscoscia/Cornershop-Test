package com.cornershop.counterstest.data.helper

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection

interface ErrorHandler {
    fun getError(throwable: Throwable): ResultWrapper.NetworkErrorEntity
}

object RemoteDataErrorHandlerImpl : ErrorHandler {
    override fun getError(throwable: Throwable): ResultWrapper.NetworkErrorEntity {
        return when (throwable) {
            is ConnectException -> ResultWrapper.NetworkErrorEntity.NoInternetConnection
            is IOException -> ResultWrapper.NetworkErrorEntity.Timeout
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ResultWrapper.NetworkErrorEntity.Timeout

                    HttpURLConnection.HTTP_NOT_FOUND -> ResultWrapper.NetworkErrorEntity.NotFound

                    HttpURLConnection.HTTP_FORBIDDEN -> ResultWrapper.NetworkErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_UNAVAILABLE,
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ResultWrapper.NetworkErrorEntity.ServiceUnavailable

                    else -> ResultWrapper.NetworkErrorEntity.UnknownHttpException
                }
            }
            else -> ResultWrapper.NetworkErrorEntity.Unknown
        }
    }
}
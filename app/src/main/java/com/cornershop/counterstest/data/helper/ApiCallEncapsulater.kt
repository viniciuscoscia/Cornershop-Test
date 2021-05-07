package com.cornershop.counterstest.data.helper

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val SAFE_API_CALL_TAG = "SAFE_API_CALL"
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Log.e(SAFE_API_CALL_TAG, "Error on safe API call", throwable)
            RemoteDataErrorHandlerImpl.getError(throwable)
        }
    }
}
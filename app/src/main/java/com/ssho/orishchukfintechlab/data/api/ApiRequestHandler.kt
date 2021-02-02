package com.ssho.orishchukfintechlab.data.api

import android.util.Log
import com.ssho.orishchukfintechlab.data.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val TAG = "ApiRequestHandler"

class ApiRequestHandler {
    suspend fun <T> handleApiRequest(dispatcher: CoroutineDispatcher, action: suspend() -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(action.invoke()).also {
                    Log.d(TAG, "Successful Api response")
                }
            } catch (throwable: Throwable) {
                ResultWrapper.NetworkError.also {
                    Log.e(TAG, "Failed Api response", throwable)
                }
            }
        }
    }
}
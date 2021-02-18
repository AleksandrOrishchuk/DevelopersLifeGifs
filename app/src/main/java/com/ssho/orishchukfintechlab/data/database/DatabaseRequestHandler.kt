package com.ssho.orishchukfintechlab.data.database

import android.util.Log
import com.ssho.orishchukfintechlab.data.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val TAG = "DatabaseRequestHandler"

class DatabaseRequestHandler {
    suspend fun <T> handleDatabaseRequest(
        dispatcher: CoroutineDispatcher,
        action: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(action())
            } catch (e: Throwable) {
                Log.e(TAG, e.message.orEmpty())
                ResultWrapper.GenericError
            }
        }
    }
}
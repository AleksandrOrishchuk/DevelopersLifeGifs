package com.ssho.orishchukfintechlab.data.api

import com.ssho.orishchukfintechlab.data.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ApiRequestHandler {
    suspend fun <T> handleApiRequest(dispatcher: CoroutineDispatcher, action: suspend() -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(action.invoke())
            } catch (throwable: Throwable) {
                ResultWrapper.NetworkError
            }
        }
    }
}
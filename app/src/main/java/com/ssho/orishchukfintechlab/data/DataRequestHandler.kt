package com.ssho.orishchukfintechlab.data

import android.util.Log

private const val TAG = "DataRequestHandler"

class DataRequestHandler {
    fun <T> handleDomainDataRequest(action: () -> T): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(action.invoke())
        }
        catch (e: IllegalStateException) {
            Log.e(TAG, e.message.orEmpty())
            ResultWrapper.NoDataError
        }
        catch (e: Throwable) {
            Log.e(TAG, e.message.orEmpty())
            ResultWrapper.GenericError
        }
    }
}
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRepository {
    suspend fun getNextGif(): ResultWrapper<ImageData>

    fun getPreviousGif(): ResultWrapper<ImageData>

    suspend fun getCurrentGif(): ResultWrapper<ImageData>

    fun getLastGif(): ResultWrapper<ImageData>

    fun isPreviousGifAvailable(): Boolean

    fun isNextGifAvailable(): Boolean
}
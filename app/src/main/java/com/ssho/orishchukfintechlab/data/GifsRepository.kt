package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRepository {
    suspend fun getNextGif(): ResultWrapper<ImageData>

    fun getPreviousGif(): ResultWrapper<ImageData>

    fun isPreviousGifCached(): Boolean
}
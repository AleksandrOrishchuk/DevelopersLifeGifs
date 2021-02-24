package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsLocalDataSource {
    fun getNextGif(): ImageData
    fun getPreviousGif(): ImageData
    suspend fun getCurrentGif(): ImageData
    fun getLastGif(): ImageData
    fun cacheImageData(imageData: ImageData)
    fun isPreviousGifCached(): Boolean
    fun isNextGifCached(): Boolean
}
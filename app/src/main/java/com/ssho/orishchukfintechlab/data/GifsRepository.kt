package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRepository {
    suspend fun getNextGif(): ImageData
    fun getPreviousGif(): ImageData
    suspend fun getCurrentGif(): ImageData
    fun getLastGif(): ImageData
    fun isPreviousGifAvailable(): Boolean
    fun isNextGifAvailable(): Boolean
}
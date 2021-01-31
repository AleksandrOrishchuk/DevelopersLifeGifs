package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsLocalDataSource {
    fun getPreviousGif(): ResultWrapper<ImageData>
    fun getNextGif(): ResultWrapper<ImageData>
    fun getCurrentGif(): ResultWrapper<ImageData>
    fun cacheImageData(imageData: ImageData)
    fun isPreviousGifCached(): Boolean
}
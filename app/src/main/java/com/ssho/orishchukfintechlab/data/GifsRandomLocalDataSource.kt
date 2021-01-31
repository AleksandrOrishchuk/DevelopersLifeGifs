package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageDataCache
import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsRandomLocalDataSource {
    var gifsCache = ImageDataCache()
        private set

    val isPreviousGifCached: Boolean get() = gifsCache.currentPosition > 0
    val isNextGifCached: Boolean get() = gifsCache.currentPosition < cachedGifsCount - 1
    private val cachedGifsCount: Int get() = gifsCache.cachedImages.size

    fun getPreviousGif(): ResultWrapper<ImageData> {
        if (!isPreviousGifCached)
            return ResultWrapper.GenericError

        val position = --gifsCache.currentPosition
        val imageData = gifsCache.cachedImages[position]

        return ResultWrapper.Success(imageData)
    }

    fun getNextGif(): ResultWrapper<ImageData> {
        if (!isNextGifCached)
            return ResultWrapper.GenericError

        val position = ++gifsCache.currentPosition
        val imageData = gifsCache.cachedImages[position]

        return ResultWrapper.Success(imageData)
    }

    fun cacheImageData(imageData: ImageData) {
        gifsCache.currentPosition++
        gifsCache.cachedImages.add(imageData)
    }

}
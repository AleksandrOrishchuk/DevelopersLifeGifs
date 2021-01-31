package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageDataCache
import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsLocalDataSourceImpl : GifsLocalDataSource {
    var gifsCache = ImageDataCache()
        private set

    private val cachedGifsCount: Int get() = gifsCache.cachedImages.size

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        if (!isPreviousGifCached())
            return ResultWrapper.GenericError

        val position = --gifsCache.currentPosition
        val imageData = gifsCache.cachedImages[position]

        return ResultWrapper.Success(imageData)
    }

    override fun getNextGif(): ResultWrapper<ImageData> {
        if (!isNextGifCached())
            return ResultWrapper.GenericError

        val position = ++gifsCache.currentPosition
        val imageData = gifsCache.cachedImages[position]

        return ResultWrapper.Success(imageData)
    }

    override fun getCurrentGif(): ResultWrapper<ImageData> {
        if (gifsCache.currentPosition < 0)
            return ResultWrapper.GenericError

        val imageData = gifsCache.cachedImages[gifsCache.currentPosition]

        return ResultWrapper.Success(imageData)
    }

    override fun cacheImageData(imageData: ImageData) {
        gifsCache.cachedImages.add(imageData)
    }

    override fun isPreviousGifCached(): Boolean = gifsCache.currentPosition > 0

    private fun isNextGifCached(): Boolean = gifsCache.currentPosition < cachedGifsCount - 1
}
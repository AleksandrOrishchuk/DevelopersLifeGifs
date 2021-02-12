package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageDataCache
import com.ssho.orishchukfintechlab.data.model.ImageData

open class GifsLocalDataSourceImpl : GifsLocalDataSource {
    internal val gifsCache = ImageDataCache()
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
            return ResultWrapper.NoDataError

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

    override fun isNextGifCached(): Boolean = gifsCache.currentPosition < cachedGifsCount - 1

    override fun getLastGif(): ResultWrapper<ImageData> {
        if (cachedGifsCount == 0)
            return ResultWrapper.GenericError

        gifsCache.currentPosition = cachedGifsCount - 1

        return getCurrentGif()
    }
}
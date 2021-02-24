package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.model.ImageData

open class GifsLocalDataSourceImpl(
    internal val gifsCache: ImageDataCache,
) : GifsLocalDataSource {

    override fun getPreviousGif(): ImageData {
        return gifsCache.getPreviousImage()
    }

    override fun getNextGif(): ImageData {
        return gifsCache.getNextImage()
    }

    override suspend fun getCurrentGif(): ImageData {
        return gifsCache.getCurrentImage()
    }

    override fun getLastGif(): ImageData {
        return gifsCache.getLastCachedImage()
    }

    override fun cacheImageData(imageData: ImageData) {
        gifsCache.cacheImage(imageData)
    }

    override fun isPreviousGifCached(): Boolean = gifsCache.isPreviousImageCached()

    override fun isNextGifCached(): Boolean = gifsCache.isNextImageCached()
}
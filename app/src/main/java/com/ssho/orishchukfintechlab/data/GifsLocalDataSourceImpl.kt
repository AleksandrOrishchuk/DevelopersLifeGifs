package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.model.ImageData

open class GifsLocalDataSourceImpl(
    internal val gifsCache: ImageDataCache,
    private val dataRequestHandler: DataRequestHandler
) : GifsLocalDataSource {

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        return dataRequestHandler.handleDomainDataRequest {
            gifsCache.getPreviousImage()
        }
    }

    override fun getNextGif(): ResultWrapper<ImageData> {
        return dataRequestHandler.handleDomainDataRequest {
            gifsCache.getNextImage()
        }
    }

    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        return dataRequestHandler.handleDomainDataRequest {
            gifsCache.getCurrentImage()
        }
    }

    override fun getLastGif(): ResultWrapper<ImageData> {
        return dataRequestHandler.handleDomainDataRequest {
            gifsCache.getLastCachedImage()
        }
    }

    override fun cacheImageData(imageData: ImageData) {
        gifsCache.cacheImage(imageData)
    }

    override fun isPreviousGifCached(): Boolean = gifsCache.isPreviousImageCached()

    override fun isNextGifCached(): Boolean = gifsCache.isNextImageCached()
}
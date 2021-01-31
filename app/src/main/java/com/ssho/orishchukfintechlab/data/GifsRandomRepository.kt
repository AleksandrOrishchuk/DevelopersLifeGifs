package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsRandomRepository(private val gifsRandomRemoteDataSource: GifsRandomRemoteDataSource,
                           private val gifsRandomLocalDataSource: GifsRandomLocalDataSource
                           ) : GifsRepository {

    private val isNextGifCached: Boolean get() = gifsRandomLocalDataSource.isNextGifCached

    override suspend fun getNextGif(): ResultWrapper<ImageData> {
        if (isNextGifCached)
            return getNextGifFromLocal()

        return getNextGifFromRemote().also {
            if (it is ResultWrapper.Success) {
                cacheGifToLocal(it.value)
            }
        }
    }

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        if (isPreviousGifCached())
            return getPreviousGifFromLocal()

        return ResultWrapper.GenericError
    }

    override fun isPreviousGifCached(): Boolean = gifsRandomLocalDataSource.isPreviousGifCached

//    suspend fun getImageData(): ResultWrapper<ImageData> {
//        return getNextGifFromRemote()
//
//    }


    private suspend fun getNextGifFromRemote() : ResultWrapper<ImageData> {
        return gifsRandomRemoteDataSource.fetchImageData()
    }

    private fun getNextGifFromLocal(): ResultWrapper<ImageData> {
        return gifsRandomLocalDataSource.getNextGif()
    }

    private fun getPreviousGifFromLocal(): ResultWrapper<ImageData> {
        return gifsRandomLocalDataSource.getPreviousGif()
    }

    private fun cacheGifToLocal(imageData: ImageData) {
        gifsRandomLocalDataSource.cacheImageData(imageData)
    }
}
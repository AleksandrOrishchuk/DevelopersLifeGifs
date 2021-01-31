package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsRepositoryImpl(private val gifsRemoteDataSource: GifsRemoteDataSource,
                         private val gifsLocalDataSource: GifsLocalDataSource
                        ) : GifsRepository {

    override suspend fun getNextGif(): ResultWrapper<ImageData> {
        val cachedResult = getNextGifFromLocal()

        if (cachedResult is ResultWrapper.Success)
            return cachedResult

        val fetchedResult = getNextGifsFromRemote()

        if (fetchedResult is ResultWrapper.Success) {
            cacheGifListToLocal(fetchedResult.value)
            return getNextGifFromLocal()
        }

        return ResultWrapper.NetworkError
    }

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        if (isPreviousGifCached())
            return getPreviousGifFromLocal()

        return ResultWrapper.GenericError
    }

    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        val currentResult = gifsLocalDataSource.getCurrentGif()

        if (currentResult is ResultWrapper.Success)
            return currentResult

        return getNextGif()
    }

    override fun isPreviousGifCached(): Boolean = gifsLocalDataSource.isPreviousGifCached()

    private suspend fun getNextGifsFromRemote(): ResultWrapper<List<ImageData>> {
        return gifsRemoteDataSource.fetchImageData()
    }

    private fun getNextGifFromLocal(): ResultWrapper<ImageData> {
        return gifsLocalDataSource.getNextGif()
    }

    private fun getPreviousGifFromLocal(): ResultWrapper<ImageData> {
        return gifsLocalDataSource.getPreviousGif()
    }

    private fun cacheGifToLocal(imageData: ImageData) {
        gifsLocalDataSource.cacheImageData(imageData)
    }

    private fun cacheGifListToLocal(imageDataList: List<ImageData>) {
        imageDataList.map {
            cacheGifToLocal(it)
        }
    }
}
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsRepositoryImpl(
    private val gifsRemoteDataSource: GifsRemoteDataSource,
    private val gifsLocalDataSource: GifsLocalDataSource
) : GifsRepository {

    override suspend fun getNextGif(): ImageData {
        if (gifsLocalDataSource.isNextGifCached())
            return getNextGifFromLocal()

        val fetchedGifs = getNextGifsFromRemote()
        cacheGifListToLocal(fetchedGifs)

        return getNextGifFromLocal()
    }

    override fun getPreviousGif(): ImageData {
        if (isPreviousGifAvailable())
            return getPreviousGifFromLocal()

        throw IllegalStateException("Previous Gif is not cached!")
    }

    override suspend fun getCurrentGif(): ImageData {
        return gifsLocalDataSource.getCurrentGif()
    }

    override fun isPreviousGifAvailable(): Boolean = gifsLocalDataSource.isPreviousGifCached()

    override fun isNextGifAvailable(): Boolean = true

    override fun getLastGif(): ImageData {
        return gifsLocalDataSource.getLastGif()
    }

    private suspend fun getNextGifsFromRemote(): List<ImageData> {
        return gifsRemoteDataSource.fetchImageData()
    }

    private fun getNextGifFromLocal(): ImageData {
        return gifsLocalDataSource.getNextGif()
    }

    private fun getPreviousGifFromLocal(): ImageData {
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
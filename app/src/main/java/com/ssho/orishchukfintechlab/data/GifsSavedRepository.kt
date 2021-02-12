package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsSavedRepository(private val gifsSavedLocalDataSource: GifsLocalDataSourceWDatabase) : GifsRepositoryWDatabase {
    override suspend fun getNextGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getNextGif()
    }

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getPreviousGif()
    }

    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getCurrentGifFromDatabase()
    }

    override fun getLastGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getLastGif()
    }

    override fun isPreviousGifCached(): Boolean {
        return gifsSavedLocalDataSource.isPreviousGifCached()
    }

    override fun isNextGifCached(): Boolean {
        return gifsSavedLocalDataSource.isNextGifCached()
    }

    override suspend fun saveGifToDatabase(imageData: ImageData) {
        gifsSavedLocalDataSource.saveGifToDatabase(imageData)
    }

    override suspend fun deleteGifFromDatabase(imageData: ImageData) {
        gifsSavedLocalDataSource.deleteGifFromDatabase(imageData)
    }

    override suspend fun isGifLiked(imageData: ImageData): Boolean {
        return gifsSavedLocalDataSource.isGifSavedToDatabase(imageData)
    }
}
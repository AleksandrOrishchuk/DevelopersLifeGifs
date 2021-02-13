package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsLikedRepository(
    private val gifsSavedLocalDataSource: GifsLocalDataSourceWDatabase
) : GifsRepositoryStoreable {
    override suspend fun getNextGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getNextGif()
    }

    override fun getPreviousGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getPreviousGif()
    }

    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getCurrentGif()
    }

    override fun getLastGif(): ResultWrapper<ImageData> {
        return gifsSavedLocalDataSource.getLastGif()
    }

    override fun isPreviousGifAvailable(): Boolean {
        return gifsSavedLocalDataSource.isPreviousGifCached()
    }

    override fun isNextGifAvailable(): Boolean {
        return gifsSavedLocalDataSource.isNextGifCached()
    }

    override suspend fun getSavedGifs(): List<ImageData> {
        return gifsSavedLocalDataSource.getGifsFromDatabase()
    }

    override suspend fun saveGif(imageData: ImageData) {
        gifsSavedLocalDataSource.saveGifToDatabase(imageData)
    }

    override suspend fun deleteGif(imageData: ImageData) {
        gifsSavedLocalDataSource.deleteGifFromDatabase(imageData)
    }

    override suspend fun isGifSaved(imageData: ImageData): Boolean {
        return gifsSavedLocalDataSource.isGifSavedToDatabase(imageData)
    }
}
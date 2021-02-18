package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.database.DatabaseRequestHandler
import com.ssho.orishchukfintechlab.data.database.SavedGifsDao
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher

class GifsLikedLocalDataSource(
    gifsCache: ImageDataCache,
    dataRequestHandler: DataRequestHandler,
    private val databaseRequestHandler: DatabaseRequestHandler,
    private val dispatcher: CoroutineDispatcher,
    private val savedGifsDao: SavedGifsDao,
    private val imageDataMapper: ImageDataMapper
) : GifsLocalDataSourceImpl(gifsCache, dataRequestHandler), GifsLocalDataSourceWDatabase {
    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        loadSavedGifsToCache()

        return super.getCurrentGif()
    }

    override suspend fun saveGifToDatabase(imageData: ImageData) {
        val imageDataEntity = imageData.let(imageDataMapper.toEntity)
        savedGifsDao.saveGif(imageDataEntity)
    }

    override suspend fun deleteGifFromDatabase(imageData: ImageData) {
        val imageDataEntity = imageData.let(imageDataMapper.toEntity)
        savedGifsDao.deleteGif(imageDataEntity)
        loadSavedGifsToCache()
    }

    override suspend fun getGifsFromDatabase(): List<ImageData> {
        val imageDataEntities = savedGifsDao.getSavedGifs()

        return imageDataEntities.map(imageDataMapper.toDomain)
    }

    override suspend fun isGifSavedToDatabase(imageData: ImageData): Boolean {
        val imageDataEntity = imageData.let(imageDataMapper.toEntity)

        val isGifSaved = databaseRequestHandler.handleDatabaseRequest(dispatcher) {
            savedGifsDao.isGifSaved(imageDataEntity.imageUrl)
        }
        return when (isGifSaved) {
            is ResultWrapper.Success -> isGifSaved.value
            else -> false
        }
    }

    private suspend fun loadSavedGifsToCache() {
        val savedGifs = databaseRequestHandler.handleDatabaseRequest(dispatcher) {
            getGifsFromDatabase()
        }
        if (savedGifs is ResultWrapper.Success)
            super.gifsCache.updateCachedImages(savedGifs.value)
    }
}
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.database.SavedGifsDao
import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsLikedLocalDataSource(
    gifsCache: ImageDataCache,
    dataRequestHandler: DataRequestHandler,
    private val savedGifsDao: SavedGifsDao,
    private val imageDataMapper: ImageDataMapper
) : GifsLocalDataSourceImpl(gifsCache, dataRequestHandler), GifsLocalDataSourceWDatabase {
    override suspend fun getCurrentGif(): ResultWrapper<ImageData> {
        loadSavedGifsToCache()

        return super.getCurrentGif()
    }

    override suspend fun saveGifToDatabase(imageData: ImageData) {
        val imageDataEntity = imageDataMapper.map(imageData)
        savedGifsDao.saveGif(imageDataEntity)
    }

    override suspend fun deleteGifFromDatabase(imageData: ImageData) {
        val imageDataEntity = imageDataMapper.map(imageData)
        savedGifsDao.deleteGif(imageDataEntity)
        loadSavedGifsToCache()
    }

    override suspend fun getGifsFromDatabase(): List<ImageData> {
        val imageDataEntities = savedGifsDao.getSavedGifs()

        return imageDataMapper.map(imageDataEntities)
    }

    override suspend fun isGifSavedToDatabase(imageData: ImageData): Boolean {
        val imageDataEntity = imageDataMapper.map(imageData)

        return savedGifsDao.isGifSaved(imageDataEntity.imageUrl)
    }

    private suspend fun loadSavedGifsToCache() {
        val savedGifs = getGifsFromDatabase()
        super.gifsCache.updateCachedImages(savedGifs)
    }
}
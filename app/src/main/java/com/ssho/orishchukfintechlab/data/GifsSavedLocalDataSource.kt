package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.database.SavedGifsDao
import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsSavedLocalDataSource(
    private val savedGifsDao: SavedGifsDao,
    private val imageDataMapper: ImageDataMapper
) : GifsLocalDataSourceImpl(), GifsLocalDataSourceWDatabase {
    override suspend fun getCurrentGifFromDatabase(): ResultWrapper<ImageData> {
        val isLocalCacheEmpty = super.gifsCache.cachedImages.isEmpty()
        if (isLocalCacheEmpty) {
            loadDatabaseGifsToCache()
            return super.getNextGif()
        }

        return super.getCurrentGif()
    }

    override suspend fun saveGifToDatabase(imageData: ImageData) {
        val imageDataEntity = imageDataMapper.map(imageData)
        savedGifsDao.saveGif(imageDataEntity)
        super.cacheImageData(imageData)
        if (super.gifsCache.currentPosition < 0)
            super.gifsCache.currentPosition++
    }

    override suspend fun deleteGifFromDatabase(imageData: ImageData) {
        val imageDataEntity = imageDataMapper.map(imageData)
        savedGifsDao.deleteGif(imageDataEntity)
        loadDatabaseGifsToCache()
        if (!super.isNextGifCached())
            super.gifsCache.currentPosition--
    }

    private suspend fun loadDatabaseGifsToCache() {
        val imageDataEntities = savedGifsDao.getSavedGifs()
        val savedGifs = imageDataMapper.map(imageDataEntities)
        super.gifsCache.cachedImages = savedGifs as MutableList<ImageData>
    }

    override suspend fun isGifSavedToDatabase(imageData: ImageData): Boolean {
        val imageDataEntity = imageDataMapper.map(imageData)

        return savedGifsDao.isGifSaved(imageDataEntity.imageUrl)
    }
}
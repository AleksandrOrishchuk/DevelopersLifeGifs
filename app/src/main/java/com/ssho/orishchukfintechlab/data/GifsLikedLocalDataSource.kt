package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.database.SavedGifsDao
import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsLikedLocalDataSource(
    gifsCache: ImageDataCache,
    private val savedGifsDao: SavedGifsDao,
    private val imageDataMapper: ImageDataMapper
) : GifsLocalDataSourceImpl(gifsCache), GifsLocalDataSourceWDatabase {
    override suspend fun getCurrentGif(): ImageData {
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

        return savedGifsDao.isGifSaved(imageDataEntity.imageUrl)
    }

    private suspend fun loadSavedGifsToCache() {
        val savedGifs = getGifsFromDatabase()

        super.gifsCache.updateCachedImages(savedGifs)
    }
}
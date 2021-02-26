package com.ssho.orishchukfintechlab.data.datasource.local

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsLocalDataSourceWDatabase : GifsLocalDataSource {
    suspend fun getGifsFromDatabase(): List<ImageData>
    suspend fun saveGifToDatabase(imageData: ImageData)
    suspend fun deleteGifFromDatabase(imageData: ImageData)
    suspend fun isGifSavedToDatabase(imageData: ImageData): Boolean
}
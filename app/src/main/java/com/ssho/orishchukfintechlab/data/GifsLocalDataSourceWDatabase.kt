package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsLocalDataSourceWDatabase : GifsLocalDataSource {
    suspend fun getCurrentGifFromDatabase(): ResultWrapper<ImageData>
    suspend fun saveGifToDatabase(imageData: ImageData)
    suspend fun deleteGifFromDatabase(imageData: ImageData)
    suspend fun isGifSavedToDatabase(imageData: ImageData): Boolean
}
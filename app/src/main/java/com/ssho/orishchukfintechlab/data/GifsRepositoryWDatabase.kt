package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRepositoryWDatabase : GifsRepository {
    suspend fun saveGifToDatabase(imageData: ImageData)
    suspend fun deleteGifFromDatabase(imageData: ImageData)
    suspend fun isGifLiked(imageData: ImageData): Boolean
}
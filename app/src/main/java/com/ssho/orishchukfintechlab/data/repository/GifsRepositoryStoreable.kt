package com.ssho.orishchukfintechlab.data.repository

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRepositoryStoreable : GifsRepository {
    suspend fun getSavedGifs(): List<ImageData>
    suspend fun saveGif(imageData: ImageData)
    suspend fun deleteGif(imageData: ImageData)
    suspend fun isGifSaved(imageData: ImageData): Boolean
}
package com.ssho.orishchukfintechlab.data.database

import androidx.room.*
import com.ssho.orishchukfintechlab.data.database.entities.ImageDataEntity

@Dao
interface SavedGifsDao {
    @Query("SELECT * FROM ImageDataEntity")
    suspend fun getSavedGifs(): List<ImageDataEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ImageDataEntity WHERE imageUrl = :imageUrl)")
    suspend fun isGifSaved(imageUrl: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveGif(imageDataEntity: ImageDataEntity)

    @Delete
    suspend fun deleteGif(imageDataEntity: ImageDataEntity)
}
package com.ssho.orishchukfintechlab.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDataEntity(
    @PrimaryKey val imageUrl: String = "",
    val imageDescription: String = ""
)
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.database.entities.ImageDataEntity
import com.ssho.orishchukfintechlab.data.model.ImageData

class ImageDataMapper {
    fun map(imageData: ImageData): ImageDataEntity {
        return ImageDataEntity(
            imageUrl = imageData.gifURL,
            imageDescription = imageData.description
        )
    }

    fun map(imageDataEntities: List<ImageDataEntity>): List<ImageData> {
        return imageDataEntities.map {
            ImageData(description = it.imageDescription, gifURL = it.imageUrl)
        }
    }
}
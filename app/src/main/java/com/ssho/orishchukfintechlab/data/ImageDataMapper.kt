package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.database.entities.ImageDataEntity
import com.ssho.orishchukfintechlab.data.model.ImageData

class ImageDataMapper(
    val toEntity: MapImageDataToEntity = MapImageDataToEntity(),
    val toDomain: MapImageDataFromEntity = MapImageDataFromEntity()
)

class MapImageDataToEntity : (ImageData) -> ImageDataEntity {
    override fun invoke(imageData: ImageData): ImageDataEntity {
        return ImageDataEntity(
            imageUrl = imageData.gifURL,
            imageDescription = imageData.description
        )
    }
}

class MapImageDataFromEntity : (ImageDataEntity) -> ImageData {
    override fun invoke(entity: ImageDataEntity): ImageData {
        return ImageData(
            description = entity.imageDescription,
            gifURL = entity.imageUrl
        )
    }
}
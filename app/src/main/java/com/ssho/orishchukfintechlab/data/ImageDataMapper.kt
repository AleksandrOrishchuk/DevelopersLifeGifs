package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.database.entities.ImageDataEntity
import com.ssho.orishchukfintechlab.data.model.ImageData

class ImageDataMapper(
    val toEntity: (ImageData) -> ImageDataEntity,
    val toDomain: (ImageDataEntity) -> ImageData,
    val fromDTO: (ImageApiDTO) -> ImageData
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

class MapImageDataFromDTO : (ImageApiDTO) -> ImageData {
    override fun invoke(imageApiDTO: ImageApiDTO): ImageData {
        val gifDescription = imageApiDTO.description
        val gifURL = imageApiDTO.gifURL.replace("http://", "https://")
        return ImageData(
            description = gifDescription,
            gifURL = gifURL
        )
    }
}
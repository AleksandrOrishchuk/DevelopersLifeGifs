package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.model.ImageData

abstract class GifsRemoteDataSourceImpl(
    private val imageDataMapper: ImageDataMapper
) : GifsRemoteDataSource {
    protected var fetchingPage: Int = 0

    override suspend fun fetchImageData(): List<ImageData> {
        val imageApiListDTO = fetchImageApiDTO()
        ++fetchingPage

        return imageApiListDTO.map(imageDataMapper.fromDTO)
    }

    protected abstract suspend fun fetchImageApiDTO(): List<ImageApiDTO>
}
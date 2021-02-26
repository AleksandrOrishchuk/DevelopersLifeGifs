package com.ssho.orishchukfintechlab.data.datasource.remote

import com.ssho.orishchukfintechlab.data.ImageDataMapper
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.model.ImageData

abstract class GifsAbstractRemoteDataSource(
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
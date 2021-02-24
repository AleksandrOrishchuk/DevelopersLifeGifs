package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO

class GifsRandomRemoteDataSource(
    private val developersLifeApi: DevelopersLifeApi,
    imageDataMapper: ImageDataMapper
) : GifsRemoteDataSourceImpl(imageDataMapper) {
    override suspend fun fetchImageApiDTO(): List<ImageApiDTO> {
        return listOf(developersLifeApi.fetchRandomImageData())
    }
}
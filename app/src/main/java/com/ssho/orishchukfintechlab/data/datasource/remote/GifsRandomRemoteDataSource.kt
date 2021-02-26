package com.ssho.orishchukfintechlab.data.datasource.remote

import com.ssho.orishchukfintechlab.data.ImageDataMapper
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO

class GifsRandomRemoteDataSource(
    private val developersLifeApi: DevelopersLifeApi,
    imageDataMapper: ImageDataMapper
) : GifsAbstractRemoteDataSource(imageDataMapper) {
    override suspend fun fetchImageApiDTO(): List<ImageApiDTO> {
        return listOf(developersLifeApi.fetchRandomImageData())
    }
}
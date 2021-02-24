package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO

class GifsTopRemoteDataSource(
    private val developersLifeApi: DevelopersLifeApi,
    imageDataMapper: ImageDataMapper
) : GifsRemoteDataSourceImpl(imageDataMapper) {
    override suspend fun fetchImageApiDTO(): List<ImageApiDTO> {
        val imageApiListDto = developersLifeApi.fetchTopImageData(super.fetchingPage.toString())
        return imageApiListDto.result
    }
}
package com.ssho.orishchukfintechlab.data.datasource.remote

import com.ssho.orishchukfintechlab.data.ImageDataMapper
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO

class GifsLatestRemoteDataSource(
    private val developersLifeApi: DevelopersLifeApi,
    imageDataMapper: ImageDataMapper
) : GifsAbstractRemoteDataSource(
    imageDataMapper = imageDataMapper
) {
    override suspend fun fetchImageApiDTO(): List<ImageApiDTO> {
        val imageApiListDto = developersLifeApi.fetchLatestImageData(super.fetchingPage.toString())
        return imageApiListDto.result
    }
}
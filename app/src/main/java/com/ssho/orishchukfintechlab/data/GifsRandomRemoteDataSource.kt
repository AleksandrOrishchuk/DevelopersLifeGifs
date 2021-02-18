package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GifsRandomRemoteDataSource(
    private val developersLifeApi: DevelopersLifeApi,
    apiRequestHandler: ApiRequestHandler,
    imageDataMapper: ImageDataMapper
) : GifsRemoteDataSourceImpl(
    apiRequestHandler = apiRequestHandler,
    imageDataMapper = imageDataMapper
) {
    override suspend fun fetchImageApiDTO(): List<ImageApiDTO> {
        return listOf(developersLifeApi.fetchRandomImageData())
    }
}
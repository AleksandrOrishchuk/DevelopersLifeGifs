package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.api.ImageApiListDTO
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class GifsRemoteDataSourceImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiRequestHandler: ApiRequestHandler,
    private val imageDataMapper: ImageDataMapper
) : GifsRemoteDataSource {
    internal var fetchingPage: Int = 0

    override suspend fun fetchImageData(): ResultWrapper<List<ImageData>> {
        return apiRequestHandler.handleApiRequest(dispatcher) {
            val imageApiListDTO = fetchImageApiDTO()
            imageApiListDTO.map(imageDataMapper.fromDTO)
        }.also {
            if (it is ResultWrapper.Success)
                fetchingPage++
        }
    }

    internal abstract suspend fun fetchImageApiDTO(): List<ImageApiDTO>
}
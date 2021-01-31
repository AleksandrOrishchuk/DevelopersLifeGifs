package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.api.ImageApiListDTO
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GifsTopRemoteDataSource(private val developersLifeApi: DevelopersLifeApi,
                              private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                              private val apiRequestHandler: ApiRequestHandler
                              ) : GifsRemoteDataSource {
    private var fetchingPage: Int = 0

    override suspend fun fetchImageData(): ResultWrapper<List<ImageData>> {
        return apiRequestHandler.handleApiRequest(dispatcher) {
            val imageApiListDTO = fetchImageApiDTO()
            mapImageApiDTO(imageApiListDTO.result)
        }.also {
            if (it is ResultWrapper.Success)
                fetchingPage++
        }
    }

    private suspend fun fetchImageApiDTO(): ImageApiListDTO {
        return developersLifeApi.fetchTopImageData(fetchingPage.toString())
    }

    private fun mapImageApiDTO(imageApiDTOList: List<ImageApiDTO>): List<ImageData> {
        return imageApiDTOList.map {
            val gifDescription = it.description
            val gifURL = it.gifURL.replace("http://", "https://")

            ImageData(
                description = gifDescription,
                gifURL = gifURL
            )
        }
    }
}
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.api.ImageApiDTO
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GifsRandomRemoteDataSource(private val developersLifeApi: DevelopersLifeApi,
                                 private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                                 private val apiRequestHandler: ApiRequestHandler) {
    suspend fun fetchImageData(): ResultWrapper<ImageData> {
        return apiRequestHandler.handleApiRequest(dispatcher) {
            val imageApiDTO = fetchImageApiDTO()
            parseImageApiDTO(imageApiDTO)
        }
    }

    private suspend fun fetchImageApiDTO(): ImageApiDTO {
        return developersLifeApi.fetchRandomImageData()
    }

    private fun parseImageApiDTO(imageApiDTO: ImageApiDTO): ImageData {
        val gifDescription = imageApiDTO.description
        val gifURL = imageApiDTO.gifURL.replace("http://", "https://")

        return ImageData(
            description = gifDescription,
            gifURL = gifURL
        )
    }
}
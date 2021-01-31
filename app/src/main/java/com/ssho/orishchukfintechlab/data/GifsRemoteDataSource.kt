package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRemoteDataSource {
    suspend fun fetchImageData(): ResultWrapper<List<ImageData>>
}
package com.ssho.orishchukfintechlab.data.datasource.remote

import com.ssho.orishchukfintechlab.data.model.ImageData

interface GifsRemoteDataSource {
    suspend fun fetchImageData(): List<ImageData>
}
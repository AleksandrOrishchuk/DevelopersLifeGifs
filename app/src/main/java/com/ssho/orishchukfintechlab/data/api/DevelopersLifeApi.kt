package com.ssho.orishchukfintechlab.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface DevelopersLifeApi {

    @GET("random?json=true")
    suspend fun fetchRandomImageData(): ImageApiDTO

    @GET("top/{page}?json=true")
    suspend fun fetchTopImageData(
        @Path(value = "page", encoded = true)
        page: String
    ): ImageApiListDTO

    @GET("latest/{page}?json=true")
    suspend fun fetchLatestImageData(
        @Path(value = "page", encoded = true)
        page: String
    ): ImageApiListDTO
}
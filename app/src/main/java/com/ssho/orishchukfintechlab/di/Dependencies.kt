package com.ssho.orishchukfintechlab.di

import com.ssho.orishchukfintechlab.data.*
import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.ui.GifsBrowserFragmentViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val developersLifeApi: DevelopersLifeApi by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developerslife.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofit.create(DevelopersLifeApi::class.java)
}

private val apiRequestHandler: ApiRequestHandler by lazy {
    ApiRequestHandler()
}

private val gifsRandomRemoteDataSource: GifsRandomRemoteDataSource by lazy {
    GifsRandomRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler
    )
}

private val gifsRandomLocalDataSource: GifsRandomLocalDataSource by lazy {
    GifsRandomLocalDataSource()
}

private val gifsRandomRepository: GifsRandomRepository by lazy {
    GifsRandomRepository(
        gifsRandomRemoteDataSource,
        gifsRandomLocalDataSource
    )
}

internal fun provideGifsBrowserViewModelFactory(): GifsBrowserFragmentViewModelFactory {
    return GifsBrowserFragmentViewModelFactory(
        GifsBrowserInteractor(gifsRandomRepository)
    )
}
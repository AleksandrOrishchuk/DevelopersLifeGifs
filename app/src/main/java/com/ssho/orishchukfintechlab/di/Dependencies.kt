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

private val gifsRandomRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsRandomRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler
    )
}

private val gifsTopRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsTopRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler
    )
}

private val gifsLatestRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsLatestRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler
    )
}

private val gifsRandomRepository: GifsRepository by lazy {
    GifsRepositoryImpl(
        gifsRemoteDataSource = gifsRandomRemoteDataSource,
        gifsLocalDataSource = GifsLocalDataSourceImpl()
    )
}

private val gifsTopRepository: GifsRepository by lazy {
    GifsRepositoryImpl(
        gifsRemoteDataSource = gifsTopRemoteDataSource,
        gifsLocalDataSource = GifsLocalDataSourceImpl()
    )
}

private val gifsLatestRepository: GifsRepository by lazy {
    GifsRepositoryImpl(
        gifsRemoteDataSource = gifsLatestRemoteDataSource,
        gifsLocalDataSource = GifsLocalDataSourceImpl()
    )
}

private val gifRepositoryProvider: GifRepositoryProvider by lazy {
    GifRepositoryProvider(
        gifsRandomRepository = gifsRandomRepository,
        gifsTopRepository = gifsTopRepository,
        gifsLatestRepository = gifsLatestRepository
    )
}

internal fun provideGifsBrowserViewModelFactory(): GifsBrowserFragmentViewModelFactory {
    return GifsBrowserFragmentViewModelFactory(
        gifsRepositoryProvider = gifRepositoryProvider
    )
}
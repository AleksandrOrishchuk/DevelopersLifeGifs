package com.ssho.orishchukfintechlab.di

import android.annotation.SuppressLint
import android.content.Context
import com.ssho.orishchukfintechlab.data.*
import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.ImageDataMapper
import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.database.SavedGifsDatabase
import com.ssho.orishchukfintechlab.ui.GifsBrowserFragmentViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
lateinit var androidContext: Context

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

private val dataRequestHandler: DataRequestHandler by lazy {
    DataRequestHandler()
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
        gifsLocalDataSource = GifsLocalDataSourceImpl(ImageDataCache(), dataRequestHandler)
    )
}

private val gifsTopRepository: GifsRepository by lazy {
    GifsRepositoryImpl(
        gifsRemoteDataSource = gifsTopRemoteDataSource,
        gifsLocalDataSource = GifsLocalDataSourceImpl(ImageDataCache(), dataRequestHandler)
    )
}

private val gifsLatestRepository: GifsRepository by lazy {
    GifsRepositoryImpl(
        gifsRemoteDataSource = gifsLatestRemoteDataSource,
        gifsLocalDataSource = GifsLocalDataSourceImpl(ImageDataCache(), dataRequestHandler)
    )
}

private val gifLikedRepository: GifsRepositoryStoreable by lazy {
    val savedGifsDatabase = SavedGifsDatabase.getExchangeRatesDatabase(androidContext)
    val savedGifsDao = savedGifsDatabase.savedGifsDao()
    GifsLikedRepository(
        GifsLikedLocalDataSource(
            gifsCache = ImageDataCache(),
            dataRequestHandler = dataRequestHandler,
            savedGifsDao = savedGifsDao,
            imageDataMapper = ImageDataMapper()
        )
    )
}

private val gifsRepositoryProvider: GifsRepositoryProvider by lazy {
    GifsRepositoryProvider(
        gifsRandomRepository = gifsRandomRepository,
        gifsTopRepository = gifsTopRepository,
        gifsLatestRepository = gifsLatestRepository,
        gifsLikedRepository = gifLikedRepository
    )
}

internal fun provideGifsBrowserViewModelFactory(): GifsBrowserFragmentViewModelFactory {
    return GifsBrowserFragmentViewModelFactory(
        gifsRepositoryProvider = gifsRepositoryProvider
    )
}
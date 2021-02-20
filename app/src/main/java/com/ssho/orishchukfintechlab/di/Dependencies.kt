package com.ssho.orishchukfintechlab.di

import android.annotation.SuppressLint
import android.content.Context
import com.ssho.orishchukfintechlab.data.*
import com.ssho.orishchukfintechlab.data.api.ApiRequestHandler
import com.ssho.orishchukfintechlab.data.api.DevelopersLifeApi
import com.ssho.orishchukfintechlab.data.ImageDataMapper
import com.ssho.orishchukfintechlab.data.cache.ImageDataCache
import com.ssho.orishchukfintechlab.data.database.DatabaseRequestHandler
import com.ssho.orishchukfintechlab.data.database.SavedGifsDatabase
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.usecase.*
import com.ssho.orishchukfintechlab.ui.GifBrowserUiMapper
import com.ssho.orishchukfintechlab.ui.GifsBrowserFragmentViewModelFactory
import kotlinx.coroutines.Dispatchers
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

private val databaseRequestHandler: DatabaseRequestHandler by lazy {
    DatabaseRequestHandler()
}

private val imageDataMapper: ImageDataMapper by lazy {
    ImageDataMapper()
}

private val gifsRandomRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsRandomRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler,
        imageDataMapper = imageDataMapper
    )
}

private val gifsTopRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsTopRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler,
        imageDataMapper = imageDataMapper
    )
}

private val gifsLatestRemoteDataSource: GifsRemoteDataSource by lazy {
    GifsLatestRemoteDataSource(
        developersLifeApi = developersLifeApi,
        apiRequestHandler = apiRequestHandler,
        imageDataMapper = imageDataMapper
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
    val savedGifsDatabase = SavedGifsDatabase.getSavedGifsDatabase(androidContext)
    val savedGifsDao = savedGifsDatabase.savedGifsDao()
    GifsLikedRepository(
        GifsLikedLocalDataSource(
            gifsCache = ImageDataCache(),
            dataRequestHandler = dataRequestHandler,
            databaseRequestHandler = databaseRequestHandler,
            dispatcher = Dispatchers.IO,
            savedGifsDao = savedGifsDao,
            imageDataMapper = imageDataMapper
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

private val gifsBrowserDomainDataMapper: GifsBrowserDomainDataMapper by lazy {
    GifsBrowserDomainDataMapper()
}

private val getNextGifUseCase: GetNextGifUseCase by lazy {
    GetNextGifUseCaseImpl(
        gifsRepositoryProvider,
        gifsBrowserDomainDataMapper
    )
}

private val getPreviousGifUseCase: GetPreviousGifUseCase by lazy {
    GetPreviousGifUseCaseImpl(
        gifsRepositoryProvider,
        gifsBrowserDomainDataMapper
    )
}

private val getCurrentGifUseCase: GetCurrentGifUseCase by lazy {
    GetCurrentGifUseCaseImpl(
        gifsRepositoryProvider,
        gifsBrowserDomainDataMapper
    )
}

private val likeGifUseCase: LikeGifUseCase by lazy {
    LikeGifUseCaseImpl(gifsRepositoryProvider)
}

private val dislikeGifUseCase: DislikeGifUseCase by lazy {
    DislikeGifUseCaseImpl(gifsRepositoryProvider)
}

internal fun provideGifsBrowserViewModelFactory(): GifsBrowserFragmentViewModelFactory {
    return GifsBrowserFragmentViewModelFactory(
        getCurrentGifUseCase = getCurrentGifUseCase,
        getNextGifUseCase = getNextGifUseCase,
        getPreviousGifUseCase = getPreviousGifUseCase,
        likeGifUseCase = likeGifUseCase,
        dislikeGifUseCase = dislikeGifUseCase,
        gifsBrowserUiMapper = GifBrowserUiMapper()
    )
}
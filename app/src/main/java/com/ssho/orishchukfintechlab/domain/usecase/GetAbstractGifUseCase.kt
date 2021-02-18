package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.GifsRepository
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

abstract class GetAbstractGifUseCase(
    private val gifsRepositoryProvider: GifsRepositoryProvider,
    private val gifsBrowserDomainDataMapper: GifsBrowserDomainDataMapper
) : GetCurrentGifUseCase,
    GetNextGifUseCase,
    GetPreviousGifUseCase {
    override suspend fun invoke(menuId: Int): ResultWrapper<GifsBrowserDomainData> {
        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)

        return when (val imageDataResponse = getImageDataResponse(gifsRepository)) {
            is ResultWrapper.Success -> {
                val gifsImageData = imageDataResponse.value
                val gifsBrowserDomainData = gifsImageData.let(gifsBrowserDomainDataMapper)
                val isCurrentGifLiked =
                    gifsRepositoryProvider
                        .getGifsSavedRepository()
                        .isGifSaved(gifsImageData)

                ResultWrapper.Success(
                    gifsBrowserDomainData.copy(
                        isNextGifAvailable = gifsRepository.isNextGifAvailable(),
                        isPreviousGifAvailable = gifsRepository.isPreviousGifAvailable(),
                        isCurrentGifLiked = isCurrentGifLiked
                    )
                )
            }
            ResultWrapper.GenericError -> ResultWrapper.GenericError
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.NoDataError -> ResultWrapper.NoDataError
        }
    }

    internal abstract suspend fun getImageDataResponse(
        gifsRepository: GifsRepository
    ): ResultWrapper<ImageData>

}
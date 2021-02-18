package com.ssho.orishchukfintechlab.domain

import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

interface GetPreviousGifUseCase {
    suspend operator fun invoke(menuId: Int): ResultWrapper<GifsBrowserDomainData>
}

class GetPreviousGifUseCaseImpl(
    private val gifsRepositoryProvider: GifsRepositoryProvider
) : GetPreviousGifUseCase {
    override suspend fun invoke(menuId: Int): ResultWrapper<GifsBrowserDomainData> {
        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)

        return when (val imageDataResponse = gifsRepository.getPreviousGif()) {
            is ResultWrapper.Success -> {
                val isCurrentGifLiked =
                    gifsRepositoryProvider
                        .getGifsSavedRepository()
                        .isGifSaved(imageDataResponse.value)
                ResultWrapper.Success(
                    GifsBrowserDomainData(
                        currentGifUrl = imageDataResponse.value.gifURL,
                        currentGifDescription = imageDataResponse.value.description,
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

}
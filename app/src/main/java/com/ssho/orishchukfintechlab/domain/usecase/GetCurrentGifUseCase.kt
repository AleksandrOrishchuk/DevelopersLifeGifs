package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.GifsRepository
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

interface GetCurrentGifUseCase {
    suspend operator fun invoke(menuId: Int): ResultWrapper<GifsBrowserDomainData>
}

class GetCurrentGifUseCaseImpl(
    gifsRepositoryProvider: GifsRepositoryProvider,
    gifsBrowserDomainDataMapper: GifsBrowserDomainDataMapper
) : GetAbstractGifUseCase(
    gifsRepositoryProvider,
    gifsBrowserDomainDataMapper
) {
    override suspend fun getImageDataResponse(
        gifsRepository: GifsRepository
    ): ResultWrapper<ImageData> {
        return gifsRepository.getCurrentGif()
    }

}
//) : GetCurrentGifUseCase {
//    override suspend fun invoke(menuId: Int): ResultWrapper<GifsBrowserDomainData> {
//        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)
//
//        return when (val imageDataResponse = gifsRepository.getCurrentGif()) {
//            is ResultWrapper.Success -> {
//                val gifsBrowserDomainData = imageDataResponse.value.let(gifsBrowserDomainDataMapper)
//                val isCurrentGifLiked =
//                    gifsRepositoryProvider
//                        .getGifsSavedRepository()
//                        .isGifSaved(imageDataResponse.value)
//
//                ResultWrapper.Success(
//                    gifsBrowserDomainData.copy(
//                        isNextGifAvailable = gifsRepository.isNextGifAvailable(),
//                        isPreviousGifAvailable = gifsRepository.isPreviousGifAvailable(),
//                        isCurrentGifLiked = isCurrentGifLiked
//                    )
//                )
//            }
//            ResultWrapper.GenericError -> ResultWrapper.GenericError
//            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
//            ResultWrapper.NoDataError -> ResultWrapper.NoDataError
//        }
//    }

//}
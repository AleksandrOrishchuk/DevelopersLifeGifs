package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.GifsRepository
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

abstract class GetAbstractGifUseCase(
    private val gifsRepositoryProvider: GifsRepositoryProvider,
    private val gifsBrowserDomainDataMapper: GifsBrowserDomainDataMapper
) : GetCurrentGifUseCase,
    GetNextGifUseCase,
    GetPreviousGifUseCase {
    override suspend fun invoke(menuId: Int): GifsBrowserDomainData {
        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)

        val gifsImageData = getImageDataResponse(gifsRepository)
        val gifsBrowserDomainData = gifsImageData.let(gifsBrowserDomainDataMapper)
        val isCurrentGifLiked =
            gifsRepositoryProvider
                .getGifsSavedRepository()
                .isGifSaved(gifsImageData)

        return gifsBrowserDomainData.copy(
            isNextGifAvailable = gifsRepository.isNextGifAvailable(),
            isPreviousGifAvailable = gifsRepository.isPreviousGifAvailable(),
            isCurrentGifLiked = isCurrentGifLiked
        )
    }

    internal abstract suspend fun getImageDataResponse(
        gifsRepository: GifsRepository
    ): ImageData

}
package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.repository.GifsRepository
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

interface GetCurrentGifUseCase {
    suspend operator fun invoke(menuId: Int): GifsBrowserDomainData
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
    ): ImageData {
        return gifsRepository.getCurrentGif()
    }
}
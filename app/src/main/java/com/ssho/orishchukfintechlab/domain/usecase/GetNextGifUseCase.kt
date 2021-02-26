package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.repository.GifsRepository
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.GifsBrowserDomainDataMapper
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

interface GetNextGifUseCase {
    suspend operator fun invoke(menuId: Int): GifsBrowserDomainData
}

class GetNextGifUseCaseImpl(
    gifsRepositoryProvider: GifsRepositoryProvider,
    gifsBrowserDomainDataMapper: GifsBrowserDomainDataMapper
) : GetAbstractGifUseCase(
    gifsRepositoryProvider,
    gifsBrowserDomainDataMapper
) {
    override suspend fun getImageDataResponse(
        gifsRepository: GifsRepository
    ): ImageData {
        return gifsRepository.getNextGif()
    }
}
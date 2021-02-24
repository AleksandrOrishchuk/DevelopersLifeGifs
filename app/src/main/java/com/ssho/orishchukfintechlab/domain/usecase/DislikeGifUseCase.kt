package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider

interface DislikeGifUseCase {
    suspend operator fun invoke(menuId: Int)
}

class DislikeGifUseCaseImpl(
    private val gifsRepositoryProvider: GifsRepositoryProvider
) : DislikeGifUseCase {
    override suspend fun invoke(menuId: Int) {
        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)
        val imageData = gifsRepository.getCurrentGif()

        gifsRepositoryProvider
            .getGifsSavedRepository()
            .deleteGif(imageData)
    }
}
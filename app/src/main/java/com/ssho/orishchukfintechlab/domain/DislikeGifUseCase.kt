package com.ssho.orishchukfintechlab.domain

import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper

interface DislikeGifUseCase {
    suspend operator fun invoke(menuId: Int)
}

class DislikeGifUseCaseImpl(
    private val gifsRepositoryProvider: GifsRepositoryProvider
) : DislikeGifUseCase {
    override suspend fun invoke(menuId: Int) {
        val gifsRepository = gifsRepositoryProvider.getGifsRepository(menuId)
        val imageDataResponse = gifsRepository.getCurrentGif()
        if (imageDataResponse is ResultWrapper.Success)
            gifsRepositoryProvider
                .getGifsSavedRepository()
                .deleteGif(imageDataResponse.value)
    }
}
package com.ssho.orishchukfintechlab.domain.usecase

import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider

interface LikeGifUseCase {
    suspend operator fun invoke(menuId:Int)
}

class LikeGifUseCaseImpl(
    private val gifsRepositoryProvider: GifsRepositoryProvider
) : LikeGifUseCase {
    override suspend fun invoke(menuId: Int) {
        val imageData = gifsRepositoryProvider.getGifsRepository(menuId).getCurrentGif()

        gifsRepositoryProvider
            .getGifsSavedRepository()
            .saveGif(imageData = imageData)
    }
}
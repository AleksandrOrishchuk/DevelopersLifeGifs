package com.ssho.orishchukfintechlab.domain

import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper

interface LikeGifUseCase {
    suspend operator fun invoke(menuId:Int)
}

class LikeGifUseCaseImpl(
    private val gifsRepositoryProvider: GifsRepositoryProvider
) : LikeGifUseCase {
    override suspend fun invoke(menuId: Int) {
        val imageDataResponse = gifsRepositoryProvider.getGifsRepository(menuId).getCurrentGif()
        if (imageDataResponse is ResultWrapper.Success)
            gifsRepositoryProvider
                .getGifsSavedRepository()
                .saveGif(imageData = imageDataResponse.value)
    }
}
package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.data.model.ImageData

class GifsBrowserInteractor(private val gifsRepository: GifsRepository) {
    val isPreviousGifCached: Boolean get() = gifsRepository.isPreviousGifCached()

    fun getPreviousGif(): ResultWrapper<ImageData> {
        return gifsRepository.getPreviousGif()
    }

    suspend fun getNextGif(): ResultWrapper<ImageData> {
        return gifsRepository.getNextGif()
    }

}
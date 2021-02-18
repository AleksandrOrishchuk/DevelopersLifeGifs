package com.ssho.orishchukfintechlab.domain.model

data class GifsBrowserDomainData(
    val currentGifUrl: String,
    val currentGifDescription: String,
    val isNextGifAvailable: Boolean,
    val isPreviousGifAvailable: Boolean,
    val isCurrentGifLiked: Boolean
)
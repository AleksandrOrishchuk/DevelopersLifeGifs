package com.ssho.orishchukfintechlab.domain.model

data class GifsBrowserDomainData(
    val currentGifUrl: String,
    val currentGifDescription: String,
    val isNextGifAvailable: Boolean = false,
    val isPreviousGifAvailable: Boolean = false,
    val isCurrentGifLiked: Boolean = false
)
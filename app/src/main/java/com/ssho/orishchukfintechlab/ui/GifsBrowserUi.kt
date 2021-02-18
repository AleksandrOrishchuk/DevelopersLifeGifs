package com.ssho.orishchukfintechlab.ui

data class GifsBrowserUi(
    val currentGifsUrl: String = "",
    val currentGifsDescription: String = "",
    val isPreviousButtonEnabled: Boolean = false,
    val isNextButtonEnabled: Boolean = false,
    val isCurrentGifLiked: Boolean = false
)
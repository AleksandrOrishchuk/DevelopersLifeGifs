package com.ssho.orishchukfintechlab.ui

import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData
import com.ssho.orishchukfintechlab.ui.model.GifsBrowserUi

class GifBrowserUiMapper : (GifsBrowserDomainData) -> GifsBrowserUi {
    override fun invoke(gifsBrowserDomainData: GifsBrowserDomainData): GifsBrowserUi {
        return with(gifsBrowserDomainData) {
            GifsBrowserUi(
                currentGifsUrl = currentGifUrl,
                currentGifsDescription = currentGifDescription,
                isNextButtonEnabled = isNextGifAvailable,
                isPreviousButtonEnabled = isPreviousGifAvailable,
                isCurrentGifLiked = isCurrentGifLiked
            )
        }
    }
}
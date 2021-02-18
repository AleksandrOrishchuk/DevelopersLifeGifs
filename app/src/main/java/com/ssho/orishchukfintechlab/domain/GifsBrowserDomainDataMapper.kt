package com.ssho.orishchukfintechlab.domain

import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData

class GifsBrowserDomainDataMapper : (ImageData) -> GifsBrowserDomainData {
    override fun invoke(imageData: ImageData): GifsBrowserDomainData {
        return GifsBrowserDomainData(
            currentGifUrl = imageData.gifURL,
            currentGifDescription = imageData.description
        )
    }
}
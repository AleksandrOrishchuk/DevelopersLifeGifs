package com.ssho.orishchukfintechlab.data.cache

import android.util.Log
import com.ssho.orishchukfintechlab.data.model.ImageData

private const val TAG = "ImageDataCache"
private const val INVALID = -1

class ImageDataCache(
    private var cachedImages: MutableList<ImageData> = mutableListOf(),
    private var currentIndex: Int = INVALID
) {
    private val isEmpty get() = cachedImages.isEmpty()

    fun getCurrentImage(): ImageData {
        if (isEmpty) throw IllegalStateException("No saved images.")

        return cachedImages[currentIndex]
    }

    fun getNextImage(): ImageData {
        return cachedImages[currentIndex + 1].also {
            currentIndex++
        }
    }

    fun getPreviousImage(): ImageData {
        return cachedImages[currentIndex - 1].also {
            currentIndex--
        }
    }

    fun getLastCachedImage(): ImageData {
        return cachedImages[cachedImages.size - 1].also {
            currentIndex = cachedImages.size - 1
        }
    }

    fun cacheImage(imageData: ImageData) {
        cachedImages.add(imageData)
    }

    fun updateCachedImages(images: List<ImageData>) {
        if (images.isEmpty())
            currentIndex = INVALID
        if (currentIndex == INVALID && images.isNotEmpty())
            currentIndex++
        if (currentIndex > images.size - 1)
            currentIndex = images.size - 1

        cachedImages = images.toMutableList()
        Log.d(TAG, "Received Images from DB, size: ${cachedImages.size}")
    }

    fun isPreviousImageCached(): Boolean = currentIndex > 0

    fun isNextImageCached(): Boolean = !isEmpty && currentIndex < cachedImages.size - 1

}

package com.ssho.orishchukfintechlab.data

import com.ssho.orishchukfintechlab.ui.TAB_LATEST
import com.ssho.orishchukfintechlab.ui.TAB_RANDOM
import com.ssho.orishchukfintechlab.ui.TAB_TOP

class GifRepositoryProvider(private val gifsRandomRepository: GifsRepository,
                            private val gifsTopRepository: GifsRepository,
                            private val gifsLatestRepository: GifsRepository) {
    fun getGifRepository(menuTab: String): GifsRepository {
        return when (menuTab) {
            TAB_RANDOM -> gifsRandomRepository
            TAB_TOP -> gifsTopRepository
            TAB_LATEST -> gifsLatestRepository
            else -> gifsRandomRepository
        }
    }
}
package com.ssho.orishchukfintechlab.data

import androidx.annotation.IdRes
import com.ssho.orishchukfintechlab.data.repository.GifsRepository
import com.ssho.orishchukfintechlab.data.repository.GifsRepositoryStoreable
import com.ssho.orishchukfintechlab.ui.TAB_LATEST
import com.ssho.orishchukfintechlab.ui.TAB_LIKED
import com.ssho.orishchukfintechlab.ui.TAB_RANDOM
import com.ssho.orishchukfintechlab.ui.TAB_TOP

class GifsRepositoryProvider(
    private val gifsRandomRepository: GifsRepository,
    private val gifsTopRepository: GifsRepository,
    private val gifsLatestRepository: GifsRepository,
    private val gifsLikedRepository: GifsRepositoryStoreable
) {
    fun getGifsRepository(@IdRes menuTab: Int): GifsRepository {
        return when (menuTab) {
            TAB_RANDOM -> gifsRandomRepository
            TAB_TOP -> gifsTopRepository
            TAB_LATEST -> gifsLatestRepository
            TAB_LIKED -> gifsLikedRepository
            else -> gifsRandomRepository
        }
    }

    fun getGifsSavedRepository(): GifsRepositoryStoreable {
        return gifsLikedRepository
    }
}
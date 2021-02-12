package com.ssho.orishchukfintechlab.data

import androidx.annotation.IdRes
import com.ssho.orishchukfintechlab.ui.TAB_LATEST
import com.ssho.orishchukfintechlab.ui.TAB_LIKED
import com.ssho.orishchukfintechlab.ui.TAB_RANDOM
import com.ssho.orishchukfintechlab.ui.TAB_TOP

class GifsRepositoryProvider(
    private val gifsRandomRepository: GifsRepository,
    private val gifsTopRepository: GifsRepository,
    private val gifsLatestRepository: GifsRepository,
    private val gifsSavedRepository: GifsRepositoryWDatabase
) {
    fun getGifsRepository(@IdRes menuTab: Int): GifsRepository {
        return when (menuTab) {
            TAB_RANDOM -> gifsRandomRepository
            TAB_TOP -> gifsTopRepository
            TAB_LATEST -> gifsLatestRepository
            TAB_LIKED -> gifsSavedRepository
            else -> gifsRandomRepository
        }
    }

    fun getGifsSavedRepository(): GifsRepositoryWDatabase {
        return gifsSavedRepository
    }
}
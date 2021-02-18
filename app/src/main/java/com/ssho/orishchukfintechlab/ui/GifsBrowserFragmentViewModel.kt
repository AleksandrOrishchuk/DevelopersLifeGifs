package com.ssho.orishchukfintechlab.ui

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.R
import com.ssho.orishchukfintechlab.domain.model.GifsBrowserDomainData
import com.ssho.orishchukfintechlab.domain.usecase.*
import kotlinx.coroutines.launch

class GifsBrowserFragmentViewModel(
    private val getCurrentGifUseCase: GetCurrentGifUseCase,
    private val getNextGifUseCase: GetNextGifUseCase,
    private val getPreviousGifUseCase: GetPreviousGifUseCase,
    private val likeGifUseCase: LikeGifUseCase,
    private val dislikeGifUseCase: DislikeGifUseCase,
    private val gifBrowserUiMapper: GifBrowserUiMapper,
    @IdRes initialMenuTabId: Int = TAB_RANDOM
) : ViewModel() {

    val viewState: LiveData<GifsBrowserViewState> get() = _viewState
    val currentMenuTabId: Int get() = _currentMenuTabId
    private val _viewState: MutableLiveData<GifsBrowserViewState> =
        MutableLiveData(GifsBrowserViewState.Loading)
    private var _currentMenuTabId = initialMenuTabId

    init {
        loadCurrentGIF()
    }

    fun onLoadNextGIFClick() {
        viewModelScope.launch {
            postLoadingViewState()
            val nextGifResult = getNextGifUseCase(currentMenuTabId)
            unwrapResultAndPostUIViewState(nextGifResult)
        }
    }

    fun onLoadPreviousGIFClick() {
        viewModelScope.launch {
            postLoadingViewState()
            val previousGifResult = getPreviousGifUseCase(currentMenuTabId)
            unwrapResultAndPostUIViewState(previousGifResult)
        }
    }

    fun onLikeCurrentGIFClick() {
        viewModelScope.launch {
            likeGifUseCase(currentMenuTabId)
        }
    }

    fun onDislikeCurrentGIFClick() {
        viewModelScope.launch {
            dislikeGifUseCase(currentMenuTabId)
            if (currentMenuTabId == TAB_LIKED)
                loadCurrentGIF()
        }
    }

    fun onMenuTabSelected(newMenuTab: Int): Boolean {
        if (newMenuTab == currentMenuTabId)
            return false

        _currentMenuTabId = newMenuTab
        loadCurrentGIF()

        return true
    }

    private fun loadCurrentGIF() {
        viewModelScope.launch {
            postLoadingViewState()
            val currentGifResult = getCurrentGifUseCase(currentMenuTabId)
            unwrapResultAndPostUIViewState(currentGifResult)
        }
    }

    private fun unwrapResultAndPostUIViewState(result: ResultWrapper<GifsBrowserDomainData>) {
        when (result) {
            is ResultWrapper.Success -> postResultViewState(result.value)
            is ResultWrapper.NetworkError -> postNetworkErrorViewState()
            is ResultWrapper.GenericError -> postGenericErrorViewState()
            is ResultWrapper.NoDataError -> postNoDataErrorViewState()

        }
    }

    private fun postLoadingViewState() {
        _viewState.postValue(GifsBrowserViewState.Loading)
    }

    private fun postResultViewState(gifsBrowserDomainData: GifsBrowserDomainData) {
        val gifsBrowserUi = gifsBrowserDomainData.let(gifBrowserUiMapper)
        _viewState.postValue(
            GifsBrowserViewState.Result(
                gifsBrowserUi
            )
        )
    }

    private fun postNetworkErrorViewState() {
        _viewState.postValue(
            GifsBrowserViewState.Error(
                message = R.string.network_error_text,
                drawableRes = R.drawable.ic_baseline_network_error_cloud,
                isRetryAvailable = true
            )
        )
    }

    private fun postGenericErrorViewState() {
        _viewState.postValue(
            GifsBrowserViewState.Error(
                message = R.string.generic_error_text,
                drawableRes = R.drawable.ic_generic_error,
                isRetryAvailable = true
            )
        )
    }

    private fun postNoDataErrorViewState() {
        _viewState.postValue(
            GifsBrowserViewState.Error(
                message = R.string.no_liked_posts_error_text,
                drawableRes = R.drawable.ic_image_not_found,
                isRetryAvailable = false
            )
        )
    }

}

@Suppress("UNCHECKED_CAST")
class GifsBrowserFragmentViewModelFactory(
    private val getCurrentGifUseCase: GetCurrentGifUseCase,
    private val getNextGifUseCase: GetNextGifUseCase,
    private val getPreviousGifUseCase: GetPreviousGifUseCase,
    private val likeGifUseCase: LikeGifUseCase,
    private val dislikeGifUseCase: DislikeGifUseCase,
    private val gifsBrowserUiMapper: GifBrowserUiMapper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GifsBrowserFragmentViewModel(
            getCurrentGifUseCase,
            getNextGifUseCase,
            getPreviousGifUseCase,
            likeGifUseCase,
            dislikeGifUseCase,
            gifsBrowserUiMapper
        ) as T
    }
}

sealed class GifsBrowserViewState {
    data class Result(
        val gifsBrowserUi: GifsBrowserUi
    ) : GifsBrowserViewState()

    data class Error(
        @StringRes val message: Int,
        @DrawableRes val drawableRes: Int,
        val isRetryAvailable: Boolean
    ) : GifsBrowserViewState()

    object Loading : GifsBrowserViewState()
}
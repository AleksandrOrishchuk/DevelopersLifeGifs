package com.ssho.orishchukfintechlab.ui

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.ssho.orishchukfintechlab.data.GifsRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifsBrowserFragmentViewModel(
    private val gifsRepositoryProvider: GifsRepositoryProvider,
    private val dispatcher: CoroutineDispatcher,
    @IdRes initialMenuTab: Int = TAB_RANDOM
) : ViewModel() {

    val viewState: LiveData<GifsBrowserViewState> get() = _viewState
    val currentMenuTab: Int get() = _currentMenuTab
    private val _viewState: MutableLiveData<GifsBrowserViewState> =
        MutableLiveData(GifsBrowserViewState.Loading)
    private var _currentMenuTab = initialMenuTab

    private val gifsRepository get() = gifsRepositoryProvider.getGifsRepository(currentMenuTab)

    init {
        loadCurrentGIF()
    }

    fun onLoadNextGIFClick() {
        viewModelScope.launch(dispatcher) {
            postLoadingViewState()

            val imageDataResponse = gifsRepository.getNextGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    fun onLoadPreviousGIFClick() {
        viewModelScope.launch(dispatcher) {
            postLoadingViewState()

            val imageDataResponse = gifsRepository.getPreviousGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    fun onLikeCurrentGIFClick() {
        viewModelScope.launch(dispatcher) {
            val imageDataResponse = gifsRepository.getCurrentGif()
            if (imageDataResponse is ResultWrapper.Success)
                gifsRepositoryProvider
                    .getGifsSavedRepository()
                    .saveGif(imageData = imageDataResponse.value)
        }
    }

    fun onDislikeCurrentGIFClick() {
        viewModelScope.launch(dispatcher) {
            val imageDataResponse = gifsRepository.getCurrentGif()
            if (imageDataResponse is ResultWrapper.Success)
                gifsRepositoryProvider
                    .getGifsSavedRepository()
                    .deleteGif(imageDataResponse.value)
            if (currentMenuTab == TAB_LIKED)
                loadCurrentGIF()
        }
    }

    fun onMenuTabSelected(newMenuTab: Int): Boolean {
        if (newMenuTab == currentMenuTab)
            return false

        _currentMenuTab = newMenuTab
        loadCurrentGIF()

        return true
    }

    private fun loadCurrentGIF() {
        viewModelScope.launch(dispatcher) {
            postLoadingViewState()

            val imageDataResponse = gifsRepository.getCurrentGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    private suspend fun unwrapImageDataResponse(result: ResultWrapper<ImageData>) {
        when (result) {
            is ResultWrapper.Success ->
                _viewState.postValue(
                    GifsBrowserViewState.Result(
                        gifImageData = result.value,
                        isPreviousButtonEnabled = gifsRepository.isPreviousGifAvailable(),
                        isNextButtonEnabled = gifsRepository.isNextGifAvailable(),
                        isCurrentGifLiked = gifsRepositoryProvider
                            .getGifsSavedRepository()
                            .isGifSaved(result.value)
                    )
                )
            is ResultWrapper.NetworkError ->
                _viewState.postValue(
                    GifsBrowserViewState.Error(
                        message = R.string.network_error_text,
                        drawableRes = R.drawable.ic_baseline_network_error_cloud,
                        isRetryAvailable = true
                    )
                )
            is ResultWrapper.GenericError ->
                _viewState.postValue(
                    GifsBrowserViewState.Error(
                        message = R.string.generic_error_text,
                        drawableRes = R.drawable.ic_generic_error,
                        isRetryAvailable = true
                    )
                )
            is ResultWrapper.NoDataError ->
                _viewState.postValue(
                    GifsBrowserViewState.Error(
                        message = R.string.no_liked_posts_error_text,
                        drawableRes = R.drawable.ic_image_not_found,
                        isRetryAvailable = false
                    )
                )
        }
    }

    private fun postLoadingViewState() {
        _viewState.postValue(GifsBrowserViewState.Loading)
    }

}

@Suppress("UNCHECKED_CAST")
class GifsBrowserFragmentViewModelFactory(
    private val gifsRepositoryProvider: GifsRepositoryProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GifsBrowserFragmentViewModel(
            gifsRepositoryProvider,
            dispatcher
        ) as T
    }
}

sealed class GifsBrowserViewState {
    data class Result(
        val gifImageData: ImageData = ImageData(),
        val isPreviousButtonEnabled: Boolean = false,
        val isNextButtonEnabled: Boolean = false,
        val isCurrentGifLiked: Boolean = false
    ) : GifsBrowserViewState()

    data class Error(
        @StringRes val message: Int,
        @DrawableRes val drawableRes: Int,
        val isRetryAvailable: Boolean
    ) : GifsBrowserViewState()

    object Loading : GifsBrowserViewState()
}
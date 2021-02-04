package com.ssho.orishchukfintechlab.ui

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.ssho.orishchukfintechlab.data.GifRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import com.ssho.orishchukfintechlab.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifsBrowserFragmentViewModel(
    private val gifsRepositoryProvider: GifRepositoryProvider,
    private val dispatcher: CoroutineDispatcher,
    initialMenuTab: String = TAB_RANDOM
    ) : ViewModel() {

    val viewState: LiveData<GifsBrowserViewState> get() = _viewState
    val currentMenuTab: String get() = _currentMenuTab
    private val _viewState: MutableLiveData<GifsBrowserViewState> = MutableLiveData(GifsBrowserViewState.Loading)
    private var _currentMenuTab = initialMenuTab

    private var gifsRepository = gifsRepositoryProvider.getGifRepository(initialMenuTab)

    init {
        onLoadNextGIFClick()
    }

    fun onLoadNextGIFClick() {
        viewModelScope.launch(dispatcher) {
            postLoadingViewState()

            val imageDataResponse = gifsRepository.getNextGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    fun onLoadPreviousGIFClick() {
        postLoadingViewState()

        val imageDataResponse = gifsRepository.getPreviousGif()
        unwrapImageDataResponse(imageDataResponse)
    }


    fun onMenuTabSelected(newMenuTab: String): Boolean {
        if (newMenuTab == currentMenuTab)
            return false

        updateGifsRepository(newMenuTab)
        loadCurrentGIF()

        _currentMenuTab = newMenuTab

        return true
    }

    private fun updateGifsRepository(newMenuTab: String) {
        gifsRepository = gifsRepositoryProvider.getGifRepository(newMenuTab)
    }

    private fun loadCurrentGIF() {
        viewModelScope.launch(dispatcher) {
            postLoadingViewState()

            val imageDataResponse = gifsRepository.getCurrentGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    private fun unwrapImageDataResponse(result: ResultWrapper<ImageData>) {
        when (result) {
            is ResultWrapper.Success ->
                _viewState.postValue(
                        GifsBrowserViewState.Result(
                                gifImageData = result.value,
                                isPreviousButtonEnabled = gifsRepository.isPreviousGifCached()
                        )
                )
            is ResultWrapper.NetworkError ->
                _viewState.postValue(
                        GifsBrowserViewState.Error(R.string.network_error_text)
                )
            is ResultWrapper.GenericError ->
                _viewState.postValue(
                        GifsBrowserViewState.Error(R.string.generic_error_text)
                )
        }
    }

    private fun postLoadingViewState() {
        _viewState.postValue(GifsBrowserViewState.Loading)
    }

}

@Suppress("UNCHECKED_CAST")
class GifsBrowserFragmentViewModelFactory(
    private val gifsRepositoryProvider: GifRepositoryProvider,
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
            val isPreviousButtonEnabled: Boolean = false) : GifsBrowserViewState()
    data class Error(@StringRes val message: Int) : GifsBrowserViewState()
    object Loading : GifsBrowserViewState()
}
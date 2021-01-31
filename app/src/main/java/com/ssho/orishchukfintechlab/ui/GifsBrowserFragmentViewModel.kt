package com.ssho.orishchukfintechlab.ui

import androidx.lifecycle.*
import com.ssho.orishchukfintechlab.data.GifsBrowserInteractor
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifsBrowserFragmentViewModel(
    private val gifsBrowserInteractor: GifsBrowserInteractor
    ) : ViewModel() {

    val viewState: LiveData<GifsBrowserViewState> get() = _viewState
    private val _viewState = MutableLiveData(GifsBrowserViewState())

    init {
        onLoadNextGIFClick()
    }

    fun onLoadNextGIFClick() {
        viewModelScope.launch(Dispatchers.IO) {
            updateViewState(_viewState.value?.copy(isLoading = true))

            val imageDataResponse = gifsBrowserInteractor.getNextGif()
            unwrapImageDataResponse(imageDataResponse)

        }
    }

    fun onLoadPreviousGIFClick() {
        updateViewState(_viewState.value?.copy(isLoading = true))

        val imageDataResponse = gifsBrowserInteractor.getPreviousGif()
        unwrapImageDataResponse(imageDataResponse)
    }

    private fun updateViewState(newViewState: GifsBrowserViewState?) {
        _viewState.postValue(newViewState)
    }

    private fun unwrapImageDataResponse(result: ResultWrapper<ImageData>) {
        when (result) {
            is ResultWrapper.Success ->
                updateViewState(
                    _viewState.value?.copy(
                        isLoading = false,
                        isNetworkError = false,
                        isGenericError = false,
                        gifImageData = result.value,
                        isPreviousButtonEnabled = gifsBrowserInteractor.isPreviousGifCached
                    )
                )
            is ResultWrapper.NetworkError ->
                updateViewState(
                    _viewState.value?.copy(
                        isLoading = false,
                        isNetworkError = true,
                        isGenericError = false
                    )
                )
            is ResultWrapper.GenericError ->
                updateViewState(
                    _viewState.value?.copy(
                        isLoading = false,
                        isNetworkError = false,
                        isGenericError = true
                    )
                )
        }
    }

}

@Suppress("UNCHECKED_CAST")
class GifsBrowserFragmentViewModelFactory(
    private val gifsBrowserInteractor: GifsBrowserInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GifsBrowserFragmentViewModel(
            gifsBrowserInteractor
        ) as T
    }
}

data class GifsBrowserViewState(
    val isLoading: Boolean = false,
    val gifImageData: ImageData = ImageData(),
    val isNetworkError: Boolean = false,
    val isGenericError: Boolean = false,
    val isPreviousButtonEnabled: Boolean = false
)
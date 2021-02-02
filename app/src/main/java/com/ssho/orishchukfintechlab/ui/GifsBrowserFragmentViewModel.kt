package com.ssho.orishchukfintechlab.ui

import androidx.lifecycle.*
import com.ssho.orishchukfintechlab.data.GifRepositoryProvider
import com.ssho.orishchukfintechlab.data.ResultWrapper
import com.ssho.orishchukfintechlab.data.model.ImageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifsBrowserFragmentViewModel(
    private val gifsRepositoryProvider: GifRepositoryProvider,
    private val dispatcher: CoroutineDispatcher
    ) : ViewModel() {

    val viewState: LiveData<GifsBrowserViewState> get() = _viewState
    val currentMenuTab: LiveData<String> get() = _currentMenuTab
    private val _viewState = MutableLiveData(GifsBrowserViewState())
    private val _currentMenuTab = MutableLiveData(TAB_RANDOM)

    private var gifsRepository = gifsRepositoryProvider.getGifRepository(TAB_RANDOM)

    init {
        onLoadNextGIFClick()
    }

    fun onLoadNextGIFClick() {
        viewModelScope.launch(dispatcher) {
            updateViewState(_viewState.value?.copy(isLoading = true))

            val imageDataResponse = gifsRepository.getNextGif()
            unwrapImageDataResponse(imageDataResponse)
        }
    }

    fun onLoadPreviousGIFClick() {
        updateViewState(_viewState.value?.copy(isLoading = true))

        val imageDataResponse = gifsRepository.getPreviousGif()
        unwrapImageDataResponse(imageDataResponse)
    }


    fun onMenuTabSelected(newMenuTab: String): Boolean {
        val currentMenuTab = currentMenuTab.value

        if (newMenuTab == currentMenuTab)
            return false

        updateGifsRepository(newMenuTab)
        loadCurrentGIF()

        _currentMenuTab.value = newMenuTab

        return true
    }

    private fun updateGifsRepository(newMenuTab: String) {
        gifsRepository = gifsRepositoryProvider.getGifRepository(newMenuTab)
    }

    private fun loadCurrentGIF() {
        viewModelScope.launch(dispatcher) {
            updateViewState(_viewState.value?.copy(isLoading = true))

            val imageDataResponse = gifsRepository.getCurrentGif()
            unwrapImageDataResponse(imageDataResponse)
        }
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
                        hasErrorOccurred = false,
                        gifImageData = result.value,
                        isPreviousButtonEnabled = gifsRepository.isPreviousGifCached()
                    )
                )
            is ResultWrapper.NetworkError ->
                updateViewState(
                    _viewState.value?.copy(
                        isLoading = false,
                        hasErrorOccurred = true,
                        isNetworkError = true,
                        isGenericError = false
                    )
                )
            is ResultWrapper.GenericError ->
                updateViewState(
                    _viewState.value?.copy(
                        isLoading = false,
                        hasErrorOccurred = true,
                        isNetworkError = false,
                        isGenericError = true
                    )
                )
        }
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

data class GifsBrowserViewState(
    val isLoading: Boolean = false,
    val gifImageData: ImageData = ImageData(),
    val hasErrorOccurred: Boolean = false,
    val isNetworkError: Boolean = false,
    val isGenericError: Boolean = false,
    val isPreviousButtonEnabled: Boolean = false
)
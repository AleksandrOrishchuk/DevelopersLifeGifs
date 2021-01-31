package com.ssho.orishchukfintechlab.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssho.orishchukfintechlab.R
import com.ssho.orishchukfintechlab.databinding.FragmentGifsBrowserBinding
import com.ssho.orishchukfintechlab.di.provideGifsBrowserViewModelFactory

private const val TAG = "GifsBrowserFragment"

class GifsBrowserFragment : Fragment() {
    companion object {
        fun newInstance(): GifsBrowserFragment {
            return GifsBrowserFragment()
        }
    }

    private lateinit var fragmentBinding: FragmentGifsBrowserBinding
    private val viewModel: GifsBrowserFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            provideGifsBrowserViewModelFactory()
        ).get(GifsBrowserFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_gifs_browser,
            container,
            false)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding.apply {

            previousButton.setOnClickListener {
                viewModel.onLoadPreviousGIFClick()
            }

            nextButton.setOnClickListener {
                viewModel.onLoadNextGIFClick()
            }

            tryAgainButton.setOnClickListener {
                viewModel.onLoadNextGIFClick()
            }

            navigationBar.setOnNavigationItemSelectedListener {
                return@setOnNavigationItemSelectedListener when (it.itemId) {
                    R.id.menu_random -> viewModel.onMenuTabSelected(TAB_RANDOM)
                    R.id.menu_top -> viewModel.onMenuTabSelected(TAB_TOP)
                    R.id.menu_latest -> viewModel.onMenuTabSelected(TAB_LATEST)
                    else -> false
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentBinding.apply {

            viewModel.currentMenuTab.observe(viewLifecycleOwner) { menuTab ->
                navigationBar.selectedItemId = when (menuTab) {
                    TAB_RANDOM -> R.id.menu_random
                    TAB_TOP -> R.id.menu_top
                    TAB_LATEST -> R.id.menu_latest
                    else -> R.id.menu_random
                }
            }

            viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
                viewState.isPreviousButtonEnabled.also {
                    previousButton.isEnabled = it
                    previousButton.isClickable = it
                }

                viewState.isNetworkError.also { isNetworkError ->
                    changeViewsVisibilityOnError(isNetworkError)
                    if (isNetworkError)
                        errorTitle.setText(R.string.network_error_text)
                }

                viewState.isGenericError.also { isGenericError ->
                    changeViewsVisibilityOnError(isGenericError)
                    if (isGenericError)
                        errorTitle.setText(R.string.generic_error_text)
                }

                loadingProgressBar.isVisible = viewState.isLoading

                gifsTitle.text = viewState.gifImageData.description

                viewState.gifImageData.gifURL.also {
                    Log.d(TAG, "Current GIF url: $it")

                    Glide.with(requireContext())
                        .load(it)
                        .placeholder(R.drawable.progress_bar)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(gifsImageView)
                }
            }
        }
    }

    private fun changeViewsVisibilityOnError(hasErrorOccurred: Boolean) {
        fragmentBinding.apply {
            errorUIContainer.isVisible = hasErrorOccurred
            gifsContentContainer.isVisible = !hasErrorOccurred
            navButtonsContainer.isVisible = !hasErrorOccurred
            navigationBar.isVisible = !hasErrorOccurred
        }
    }
}
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
            false
        )

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
                viewModel.onMenuTabSelected(it.itemId)
            }

            navigationBar.selectedItemId = viewModel.currentMenuTabId

            likedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    viewModel.onLikeCurrentGIFClick()
                else
                    viewModel.onDislikeCurrentGIFClick()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            applyViewState(viewState)
        }

    }

    private fun applyViewState(viewState: GifsBrowserViewState) {
        fragmentBinding.apply {

            gifsContentContainer.isVisible = viewState is GifsBrowserViewState.Result
            navButtonsContainer.isVisible = viewState is GifsBrowserViewState.Result

            errorUIContainer.isVisible = viewState is GifsBrowserViewState.Error
            loadingProgressBar.isVisible = viewState is GifsBrowserViewState.Loading

            when (viewState) {
                is GifsBrowserViewState.Result -> {
                    with(viewState.gifsBrowserUi) {
                        previousButton.isEnabled = isPreviousButtonEnabled
                        previousButton.isClickable = isPreviousButtonEnabled
                        nextButton.isEnabled = isNextButtonEnabled
                        nextButton.isClickable = isNextButtonEnabled
                        likedCheckbox.isChecked = isCurrentGifLiked

                        gifsTitle.text = currentGifsDescription

                        Log.d(TAG, "Current GIF url: ${currentGifsUrl}")

                        Glide.with(requireContext())
                            .load(currentGifsUrl)
                            .placeholder(R.drawable.progress_bar)
                            .error(R.drawable.ic_baseline_network_error_cloud)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(gifsImageView)
                    }
                }
                is GifsBrowserViewState.Error -> {
                    errorTitle.setText(viewState.message)
                    errorImageView.setImageResource(viewState.drawableRes)
                    tryAgainButton.isVisible = viewState.isRetryAvailable
                }
                GifsBrowserViewState.Loading -> Unit
            }
        }
    }
}
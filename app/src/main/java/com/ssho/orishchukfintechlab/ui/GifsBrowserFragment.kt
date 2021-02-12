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
                return@setOnNavigationItemSelectedListener viewModel.onMenuTabSelected(it.itemId)
            }

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

        fragmentBinding.navigationBar.selectedItemId = viewModel.currentMenuTab

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
                    previousButton.isEnabled = viewState.isPreviousButtonEnabled
                    previousButton.isClickable = viewState.isPreviousButtonEnabled
                    nextButton.isEnabled = viewState.isNextButtonEnabled
                    likedCheckbox.isChecked = viewState.isCurrentGifLiked

                    gifsTitle.text = viewState.gifImageData.description

                    viewState.gifImageData.gifURL.also {
                        Log.d(TAG, "Current GIF url: $it")

                        Glide.with(requireContext())
                                .load(it)
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
                GifsBrowserViewState.Loading -> loadingProgressBar
            }
        }
    }
}
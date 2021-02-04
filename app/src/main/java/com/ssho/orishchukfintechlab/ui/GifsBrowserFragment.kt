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

        fragmentBinding.navigationBar.selectedItemId = when (viewModel.currentMenuTab) {
            TAB_RANDOM -> R.id.menu_random
            TAB_TOP -> R.id.menu_top
            TAB_LATEST -> R.id.menu_latest
            else -> R.id.menu_random
        }

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
                is GifsBrowserViewState.Error -> errorTitle.setText(viewState.message)
                GifsBrowserViewState.Loading -> loadingProgressBar
            }
        }
    }
}
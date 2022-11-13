package com.tanasi.sflix.fragments.tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tanasi.sflix.R
import com.tanasi.sflix.adapters.SflixAdapter
import com.tanasi.sflix.databinding.FragmentTvShowsBinding
import com.tanasi.sflix.models.TvShow

class TvShowsFragment : Fragment() {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()

    private val sflixAdapter = SflixAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
            viewModel.getTvShows()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTvShows()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                TvShowsViewModel.State.Loading -> binding.isLoading.root.visibility = View.VISIBLE

                is TvShowsViewModel.State.SuccessLoading -> {
                    displayTvShows(state.tvShows)
                    binding.isLoading.root.visibility = View.GONE
                }
                is TvShowsViewModel.State.FailedLoading -> {
                    Toast.makeText(
                        requireContext(),
                        state.error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun initializeTvShows() {
        binding.vgvTvShows.apply {
            adapter = sflixAdapter
            setItemSpacing(requireContext().resources.getDimension(R.dimen.tv_shows_spacing).toInt())
        }
    }

    private fun displayTvShows(tvShows: List<TvShow>) {
        sflixAdapter.items.apply {
            clear()
            addAll(tvShows.onEach {
                it.itemType = SflixAdapter.Type.TV_SHOW_GRID_ITEM
            })
        }
        sflixAdapter.notifyDataSetChanged()
    }
}
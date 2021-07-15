package ir.nwise.app.ui.home

import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.nwise.app.R
import ir.nwise.app.databinding.FragmentHomeBinding
import ir.nwise.app.ui.base.BaseFragment
import ir.nwise.app.ui.error.ErrorType
import ir.nwise.app.ui.utils.hide
import ir.nwise.app.ui.utils.show
import ir.nwise.app.ui.widget.ErrorView
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewState, HomeViewModel, FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModel()
    private var errorView: ErrorView? = null

    private val photoAdapter: PhotoAdapter = PhotoAdapter { model ->
        binding.root.findNavController().navigate(
            HomeFragmentDirections.openDetail(model)
        )
    }

    override fun getLayout(): Int = R.layout.fragment_home

    override fun render(state: HomeViewState) {
        binding.apply {
            swipeRefresh.isRefreshing = false
            spinner.hide()
            errorView?.hide()
            when (state) {
                is HomeViewState.Loading -> {
                    if (photoAdapter.itemCount == 0) {
                        spinner.show()
                        recyclerView.hide()
                    }
                }
                is HomeViewState.Loaded -> {
                    recyclerView.show()
                    errorView?.hide()
                    spinner.hide()
                    recyclerView.show()
                    photoAdapter.submitItems(state.photos)
                    photoAdapter.notifyDataSetChanged()
                }
                is HomeViewState.Error -> {
                    recyclerView.hide()
                    errorView?.show()
                    swipeRefresh.isRefreshing = false
                    spinner.hide()
                    Log.e(
                        "HomeFragment",
                        state.throwable.message,
                        state.throwable
                    )
                    errorView?.show(ErrorType.fromThrowable(state.throwable))
                }
            }
        }
    }

    override fun onCreateViewCompleted(savedInstanceState: Bundle?) {
        viewModel = homeViewModel
        setHasOptionsMenu(true)
        initRecyclerView()
        setupSwipeRefreshLayout()
        errorView = activity?.findViewById(R.id.error_view)
        errorView?.setButtonListener { viewModel.getPhotos() }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = photoAdapter
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getPhotos()
        }
    }
}
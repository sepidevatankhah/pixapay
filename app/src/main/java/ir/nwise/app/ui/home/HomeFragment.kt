package ir.nwise.app.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
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

class HomeFragment : BaseFragment<HomeViewState, HomeViewModel, FragmentHomeBinding>(),
    SearchView.OnQueryTextListener {
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
        errorView?.setButtonListener { viewModel.getPhotos(getString(R.string.search_filter_default_value)) }
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
            viewModel.getPhotos(getString(R.string.search_filter_default_value))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            isIconifiedByDefault = false // Do not iconify the widget; expand it by default
            queryHint = getString(R.string.search_view_hint)
            val query =
                if (viewModel.cachedFilter.isEmpty()) getString(R.string.search_filter_default_value) else viewModel.cachedFilter
            onQueryTextChange(query)
            setQuery(query, true)
            minimumWidth = 2700000
            isSubmitButtonEnabled = true
        }.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { newText ->
//            Timber.d("query : %s", newText)
            if (newText.trim().replace(" ", "").length >= 3 || newText.isEmpty()) {
                viewModel.cachedFilter = newText
                viewModel.getPhotos(newText)
//                viewModel.createLiveData()
                startObserving()

            }
        }
        return true
    }
}
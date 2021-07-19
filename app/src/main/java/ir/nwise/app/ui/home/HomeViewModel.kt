package ir.nwise.app.ui.home

import androidx.lifecycle.viewModelScope
import ir.nwise.app.common.NoInternetConnectionException
import ir.nwise.app.domain.NetworkManager
import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.domain.usecase.GetPhotoResultUseCase
import ir.nwise.app.domain.usecase.base.UseCaseResult
import ir.nwise.app.ui.base.BaseViewModel

class HomeViewModel(
    private val getPhotoResultUseCase: GetPhotoResultUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel<HomeViewState>() {


    var cachedFilter: String = ""

    internal fun getPhotos(filter: String) {
        getPhotoResultUseCase.execute(
            viewModelScope,
            PhotoModel(query = if (cachedFilter.isEmpty()) filter else cachedFilter)
        ) {
            when (this) {
                is UseCaseResult.Loading -> liveData.postValue(HomeViewState.Loading)
                is UseCaseResult.Success -> liveData.postValue(HomeViewState.Loaded(this.data))
                is UseCaseResult.Error -> {
                    var exception = this.exception
                    if (networkManager.hasNetwork().not())
                        exception = NoInternetConnectionException()
                    liveData.postValue(HomeViewState.Error(exception))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getPhotoResultUseCase.unsubscribe()
    }
}

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(val photos: List<PhotoResponse>) : HomeViewState()
    data class Error(val throwable: Throwable) : HomeViewState()
}
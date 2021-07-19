package ir.nwise.app.domain.usecase

import ir.nwise.app.data.DispatcherProvider
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.domain.usecase.base.UseCase

class GetAllCachedPhotoUseCase(
    private val appRepository: AppRepository,
    dispatchers: DispatcherProvider
) : UseCase<Any, MutableList<PhotoResponse>>(dispatchers) {
    override suspend fun executeOnBackground(param: Any?): MutableList<PhotoResponse> {
        return appRepository.getAllPhotosFromCache()
    }
}
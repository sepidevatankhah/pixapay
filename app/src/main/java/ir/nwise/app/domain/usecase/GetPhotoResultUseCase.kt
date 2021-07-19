package ir.nwise.app.domain.usecase

import ir.nwise.app.data.DispatcherProvider
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.domain.usecase.base.UseCase


class GetPhotoResultUseCase(
    private val appRepository: AppRepository,
    dispatchers: DispatcherProvider
) : UseCase<PhotoModel, List<PhotoResponse>>(dispatchers) {
    override suspend fun executeOnBackground(param: PhotoModel?): List<PhotoResponse> {
        return param?.let { appRepository.getPhotoResult(it) } ?: emptyList()
    }
}
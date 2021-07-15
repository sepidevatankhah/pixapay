package ir.nwise.app.domain.usecase

import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.usecase.base.UseCase


class GetPhotoResultUseCase(private val photoRepo: AppRepository) : UseCase<List<PhotoResponse>>() {
    override suspend fun executeOnBackground(): List<PhotoResponse> {
        return photoRepo.getPhotoResult()
    }
}
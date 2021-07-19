package ir.nwise.app.domain.usecase

import ir.nwise.app.data.DispatcherProvider
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.models.BasePhoto
import ir.nwise.app.domain.usecase.base.UseCase

class InsertPhotoUseCase(
    private val appRepository: AppRepository,
    dispatchers: DispatcherProvider
) : UseCase<Any, Long>(dispatchers) {
    //TODO: TO remove this
    //Add in app repository
    var basePhoto = BasePhoto(1, 50, listOf())
    override suspend fun executeOnBackground(param: Any?): Long {
        return appRepository.savePhotos(basePhoto)
    }
}
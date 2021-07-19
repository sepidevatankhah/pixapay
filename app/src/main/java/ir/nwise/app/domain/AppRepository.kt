package ir.nwise.app.domain

import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.PhotoResponse

interface AppRepository {
    suspend fun getPhotoResult(param: PhotoModel): List<PhotoResponse>
}
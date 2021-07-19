package ir.nwise.app.data.repository

import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.networking.ApiService

class AppRepositoryImp(private val apiService: ApiService) : AppRepository {
    override suspend fun getPhotoResult(param: PhotoModel): List<PhotoResponse> {
        return apiService.getPhotos(param.query, param.pageSize, param.pageNum).await().photoList
    }
}
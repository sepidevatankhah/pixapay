package ir.nwise.app.data.repository

import android.content.Context
import ir.nwise.app.data.Util.NetworkManager.isOnline
import ir.nwise.app.database.PhotoDao
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.BasePhoto
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.networking.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepositoryImp(
    private val apiService: ApiService,
    private val photoDao: PhotoDao,
    private val context: Context
) : AppRepository {
    override suspend fun getPhotoResult(param: PhotoModel): List<PhotoResponse> {
        return if (isOnline(context)) {
            val response = apiService.getPhotos(param.query, param.pageSize, param.pageNum).await()
            if (getAllPhotosFromCache().isEmpty())
                savePhotos(response)
            response.photoList
        } else {
            getAllPhotosFromCache()
        }
    }

    override suspend fun savePhotos(basePhoto: BasePhoto): Long {
        if (basePhoto.photoList.isNotEmpty()) {
            for (photo in basePhoto.photoList) {
                withContext(Dispatchers.IO) { photoDao.insertPhoto(photo) }
            }
        }
        return 0L
    }

    override suspend fun getAllPhotosFromCache(): MutableList<PhotoResponse> {
        return withContext(Dispatchers.IO) {
            photoDao.selectAll()
        }
    }
}
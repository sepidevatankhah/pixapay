package ir.nwise.app.networking

import ir.nwise.app.domain.models.BasePhoto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/?")
    fun getPhotos(
        @Query("q") query: String?,
        @Query("per_page") pageSize: Int?,
        @Query("page") pageNum: Int?
    ): Deferred<BasePhoto>
}
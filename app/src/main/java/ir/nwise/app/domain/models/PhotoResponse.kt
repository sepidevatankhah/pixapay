package ir.nwise.app.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val userName: String,
    @SerializedName("largeImageURL") val largeImageUrl: String,
    @SerializedName("webformatURL") val webFormatUrl: String,
    @SerializedName("previewURL") val previewImageUrl: String,
    @SerializedName("userImageURL") val userImageUrl: String,
    @SerializedName("comments") val commentNumber: String,
    @SerializedName("likes") val likeNumber: String,
    @SerializedName("tags") val tags: String,
    @SerializedName("downloads") val downloadNumber: String
) : Parcelable
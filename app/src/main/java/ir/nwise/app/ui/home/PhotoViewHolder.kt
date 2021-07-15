package ir.nwise.app.ui.home

import android.view.View
import ir.nwise.app.databinding.ItemPhotoBinding
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.ui.base.BaseViewHolder
import ir.nwise.app.ui.utils.loadUrl


class PhotoViewHolder(
    val binding: ItemPhotoBinding,
    private val onItemClicked: (PhotoResponse) -> Unit
) : BaseViewHolder<PhotoResponse>(binding) {

    override fun bind(model: PhotoResponse) {
        with(model)
        {
            binding.apply {
                txtUserName.text = userName
                imgPreview.loadUrl(previewImageUrl)
                txtLikes.text = likeNumber
                txtDownload.text = downloadNumber
                txtUserName.text = userName
                txtView.text = viewNumber
                root.setOnClickListener { onItemClicked.invoke(model) }
            }
        }
    }
}



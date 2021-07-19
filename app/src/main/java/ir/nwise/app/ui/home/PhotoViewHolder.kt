package ir.nwise.app.ui.home

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
                val tagsBuilder = StringBuilder()
                tags.split(", ").forEach {
                    tagsBuilder.append(it + "\n")
                }

                txtUserName.text = userName
                imgPreview.loadUrl(previewImageUrl)
                txtTags.text = tagsBuilder
                txtUserName.text = userName
                root.setOnClickListener { onItemClicked.invoke(model) }
            }
        }
    }
}



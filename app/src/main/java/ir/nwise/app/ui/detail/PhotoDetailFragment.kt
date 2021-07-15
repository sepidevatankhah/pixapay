package ir.nwise.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ir.nwise.app.R
import ir.nwise.app.databinding.FragmentDetailBinding
import ir.nwise.app.ui.utils.loadUrl

class PhotoDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: PhotoDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        showDetail()
        return binding.root
    }


    private fun showDetail() {
        binding.apply {
            args.photo?.let {
                imgLargePhoto.loadUrl(it.largeImageUrl)
                txtTags.text = it.tags
                txtUserName.text = it.userName

            }
        }
    }

}
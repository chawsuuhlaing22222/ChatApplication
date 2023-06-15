package com.padc.chatapplication.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.padc.chatapplication.R
import kotlinx.android.synthetic.main.fragment_gallery_or_camera_choose.*


class GalleryOrCameraChooseFragment : DialogFragment() {

    private var REQUEST_PICK_IMAGE_FROM_GALLERY=1001
    private var REQUEST_PICK_IMAGE_FROM_CAMERA=1002

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_or_camera_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpActionListener()
    }

    private fun setUpActionListener() {
        rbCamera.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, REQUEST_PICK_IMAGE_FROM_CAMERA)
            }
        }

        rbGallery.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, REQUEST_PICK_IMAGE_FROM_GALLERY)
            }
        }
    }



   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE_FROM_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            ivProfile.setImageURI(imageUri)
            filePath = imageUri
            changeImageToBitmap()

            //change image
            bitmap?.let { mPresenter.updateImage(it,mUserVO) }
        }
    }*/


}
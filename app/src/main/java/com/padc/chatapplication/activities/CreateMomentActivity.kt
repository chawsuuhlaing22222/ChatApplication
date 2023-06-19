package com.padc.chatapplication.activities

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.PostAddedImgAdapter
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.impls.MomentPresenterImpl
import com.padc.chatapplication.mvp.views.MomentView
import kotlinx.android.synthetic.main.activity_create_moment.*
import kotlinx.android.synthetic.main.view_holder_choose_image_from_dialog.view.*
import java.io.IOException
import java.util.*


class CreateMomentActivity : AppCompatActivity(), MomentView {
    lateinit var mAddedImageAdapter: PostAddedImgAdapter
    private var REQUEST_PICK_IMAGE_FROM_GALLERY = 1001
    private var REQUEST_PICK_IMAGE_FROM_CAMERA = 100


    private var filePath: Uri? = null
    private var bitmap: Bitmap? = null

    private var mUploadImages: ArrayList<String> = arrayListOf()
    lateinit var mMomentPresenter: MomentPresenterImpl
    lateinit var mUserVO: UserVO

    val permissions = arrayOf(android.Manifest.permission.CAMERA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_moment)

        mUploadImages = arrayListOf()
        setUpPresenter()
        setUpRecycler()
        mMomentPresenter.onUiReady(this, this)
        setUpActionListener()

    }

    private fun setUpPresenter() {
        mMomentPresenter = ViewModelProvider(this)[MomentPresenterImpl::class.java]
        mMomentPresenter.initPresenter(this)
    }

    private fun setUpActionListener() {
        ivCancelMoment.setOnClickListener {
            finish()
        }
        btnAddImage.setOnClickListener {
            showChooseAlert()
        }

        btnCreate.setOnClickListener {

            var desc = edtMomentDesInMomentCreate.text.toString()
            val time = System.currentTimeMillis()
            val uploadImages = mUploadImages.joinToString(",")
            var moment = MomentVO(
                "${UUID.randomUUID()}", mUserVO.name,
                mUserVO.profileImage, desc, uploadImages, time.toString(), mUserVO.qrCode.toString()
            )
            mMomentPresenter.onTapCreateMoment(moment)
            finish()
        }
    }

    private fun setUpRecycler() {
        mAddedImageAdapter = PostAddedImgAdapter()
        rvAddedImgs.apply {
            adapter = mAddedImageAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.left = 20
                }
            })
        }
    }


    private fun showChooseAlert() {
        // var  dialog= AlertDialog.Builder
        var dialog = Dialog(this)
        var view =
            LayoutInflater.from(this).inflate(R.layout.view_holder_choose_image_from_dialog, null)
        // view.minimumWidth = ((window.decorView.width* 0.9f).toInt())
        //  view.setMinimumHeight(((window.decorView.height * 0.9f).toInt()))
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = ((displayWidth * 0.9f).toInt())
        layoutParams.height = ((displayHeight * 0.5f).toInt())
        view.layoutParams = layoutParams


        dialog.setContentView(view)
        dialog?.window?.setLayout(
            layoutParams.width,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
        view.btnCanmera.setOnClickListener {

            if (hasPermission()) {

                performCameraTakePhoto()
                dialog.dismiss()
            } else {
                requestPermission()
                dialog.dismiss()
            }
            dialog.dismiss()

        }

        view.btnGallery.setOnClickListener {

            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_PICK_IMAGE_FROM_GALLERY)
            dialog.dismiss()

        }

    }

    fun performCameraTakePhoto() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "take_picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "take_picture_des")
        filePath = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
        startActivityForResult(intent, REQUEST_PICK_IMAGE_FROM_CAMERA)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PICK_IMAGE_FROM_CAMERA ->{
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()

                    performCameraTakePhoto()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()

                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE_FROM_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            filePath = imageUri


        }
        if (requestCode == REQUEST_PICK_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {

            // val imageUri = data?.data
            // filePath = imageUri
            //  val imageBitmap = data?.extras?.get("data") as Bitmap
            // mMomentPresenter.uploadImage(imageBitmap)
        }

        changeImageToBitmap()
        bitmap?.let { mMomentPresenter.uploadImage(it) }
    }

    private fun changeImageToBitmap() {
        try {

            filePath?.let { file ->
                if (Build.VERSION.SDK_INT >= 29) {

                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, file)
                    bitmap = ImageDecoder.decodeBitmap(source)


                } else {

                    bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver, filePath
                    )
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun saveImageUrl(img: String) {
        mUploadImages.add(img)
        mAddedImageAdapter.setNewData(mUploadImages)
        Log.i("upload_img", img)
    }

    override fun saveCurrentUserInfo(userVO: UserVO) {
        mUserVO = userVO
        Glide.with(this).load(mUserVO.profileImage).into(ivProfileInMomentCreate)
        tvNameCreate.text = mUserVO.name
    }

    override fun showAllMoments(moments: List<MomentVO>) {

    }

    override fun showError(error: String) {
        showError(error)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PICK_IMAGE_FROM_CAMERA)

    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}



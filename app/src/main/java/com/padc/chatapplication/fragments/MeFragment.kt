package com.padc.chatapplication.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.installations.FirebaseInstallations
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.NewMomentAdapter
//import com.padc.chatapplication.adapters.MomentAdapter
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.impls.MePresenterImpl
import com.padc.chatapplication.mvp.views.MeView
import com.padc.chatapplication.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_me.ivProfile
import kotlinx.android.synthetic.main.view_item_qr_code_bitmap.view.*
import java.io.IOException


class MeFragment : Fragment(),MeView {
    val QRcodeWidth =120
    private var fbToken=""
    lateinit var mPresenter:MePresenterImpl
    lateinit var mUserVO: UserVO

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null

    private var qrCode:Bitmap?=null

    private var REQUEST_PICK_IMAGE=300
    lateinit var mMomentAdapter:NewMomentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.padc.chatapplication.R.layout.fragment_me, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //for short toekn
        FirebaseInstallations.getInstance().id.addOnCompleteListener {
            fbToken=it.result

        }
        setUpPresenter()
        setUpRecycler()
        setUpListener()
        context?.let { mPresenter.onUiReady(it,this) }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            ivProfile.setImageURI(imageUri)
            filePath = imageUri
            changeImageToBitmap()

            //change image
            bitmap?.let { mPresenter.updateImage(it,mUserVO) }
        }
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[MePresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpListener() {
        btnCreateMoment.setOnClickListener {

           var userJson=Gson().toJson(mUserVO)
            //var frag=
            ProfileInfoDialogFragment.newFragment(userJson).show(childFragmentManager,"edit")


        }

        ivQrCodeInProfile.setOnClickListener {

            var dialog= context?.let { it1 -> Dialog(it1) }
            var view=LayoutInflater.from(context).inflate(R.layout.view_item_qr_code_bitmap,null)
            dialog?.setContentView(view)
            dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.show()

            view.ivQRCode.setImageBitmap(qrCode)


        }

        ivProfile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_PICK_IMAGE)
        }
    }

    private fun setUpRecycler() {
          mMomentAdapter = NewMomentAdapter()
        rvMomentsInProfile.apply {
            setEmptyView(tvEmptyMyMoments)
            adapter=mMomentAdapter
            layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top=40
                }
            })
        }
    }

    @Throws(WriterException::class)
    fun textToImageEncode(Value: String?): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    override fun showUserInfo(userVO: UserVO) {

        mUserVO=userVO
        context?.let { Glide.with(it).load(userVO.profileImage).into(ivProfile) }
        tvProfileName.text=userVO.name
        tvPhoneNo.text=userVO.phoneNo
        tvDate.text=userVO.dob
        tvGenderInProfile.text=userVO.gender

        //qr code
        qrCode=textToImageEncode(userVO.qrCode)
        //var imgBitmap=qrCode
        ivQrCodeInProfile.setImageBitmap(qrCode)

        ConfigUtils.getInstance().saveUserName(userVO.name)

    }

    override fun showMyMoments(moments: List<MomentVO>) {
        mMomentAdapter.submitList(moments.sortedByDescending { moment->moment.uploadDate })
        mMomentAdapter.notifyDataSetChanged()
    }

    override fun showError(error: String) {
        showError(error)
    }


    private fun changeImageToBitmap(){
        try {

            filePath?.let {file->
                if (Build.VERSION.SDK_INT >= 29) {
                    activity?.contentResolver?.let{
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource(it, file)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }


                } else {
                    activity?.contentResolver?.let{
                        bitmap = MediaStore.Images.Media.getBitmap(
                            it, filePath
                        )
                    }



                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
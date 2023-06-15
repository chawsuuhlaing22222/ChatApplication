package com.padc.chatapplication.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.ChatMessageAdapter
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.PeerToPeerChatPresenter
import com.padc.chatapplication.mvp.presenters.impls.PeerToPeerChatPresenterImpl
import com.padc.chatapplication.mvp.views.PeerToPeerChatView
import kotlinx.android.synthetic.main.activity_conversation_detail.*
import java.io.IOException


class PeerToPeerChatActivity : AppCompatActivity() ,PeerToPeerChatView{

    lateinit var mChatAdapter:ChatMessageAdapter
    val REQUEST_IMAGE_CAPTURE=100
    private val REQUEST_IMAGE_GALLERY = 101
    private val REQUEST_IMAGE_GIF = 102
    val permissions= arrayOf(android.Manifest.permission.CAMERA)
    lateinit var receiptUser:UserVO
    lateinit var mPresenter:PeerToPeerChatPresenter
    private var mUserVO:UserVO?=null

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null
    var senderId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    companion object{

        const val IEXTRA_RECEIPT="receipt"
        fun newIntent(context: Context,receipt:String):Intent{

            var intent=Intent(context,PeerToPeerChatActivity::class.java)
            intent.putExtra(IEXTRA_RECEIPT,receipt)
            return intent

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_detail)

        setUpPresenter()
        setData()
        setUpRecycler()
        setUpActionListener()

        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        mPresenter.onUiReady(this,this,currentUserId ?: "",receiptUser.qrCode ?: "")

    }
    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[PeerToPeerChatPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }
    private fun setData() {

        var receiptJson=intent.getStringExtra(IEXTRA_RECEIPT)
        var typeToken=object : TypeToken<UserVO>(){}.type
        receiptUser =Gson().fromJson(receiptJson,typeToken)

        Glide.with(this).load(receiptUser.profileImage).into(ivPersonInChatDetail)
        tvNameInChatDetail.text=receiptUser.name



    }


    private fun setUpActionListener() {
        ivGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_IMAGE_GALLERY)
           // startActivity(intent)
        }

        ivCamera.setOnClickListener {
            if(hasPermission()){

                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "take_picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "take_picture_des")
                filePath = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                // Create camera intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }else{
                requestPermission()
            }
        }

        ivSendMsg.setOnClickListener {

            var msg=edtTypeMsg.text.toString()
            var sendTime=System.currentTimeMillis().toString()
            mPresenter.onTapSendMessage(MessageVO(sendTime,msg,senderId,receiptUser.qrCode,null))
            edtTypeMsg.setText("")
        }

        ivGif.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_IMAGE_GIF)
        }
    }

    private fun setUpRecycler() {
            mChatAdapter = ChatMessageAdapter()
            mChatAdapter.setReceipt(receiptUser)
            rvChatMessage.apply {
                adapter=mChatAdapter
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            filePath = imageUri

            val type = filePath?.let { contentResolver.getType(it) }
            Log.i("image",type.toString())
            if ( type?.startsWith("image/") == true && type!="image/gif") {
                changeImageToBitmap()
                var msg=MessageVO(System.currentTimeMillis().toString(),null,senderId,receiptUser.qrCode,null,null,"image")
                bitmap?.let { mPresenter.onTapSendImage(it,msg) }
            }else{
                 showError("Please choose image")
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            changeImageToBitmap()
            var msg=MessageVO(System.currentTimeMillis().toString(),null,senderId,receiptUser.qrCode,null,null,"image")
            bitmap?.let { mPresenter.onTapSendImage(it,msg) }
        }

        if(requestCode == REQUEST_IMAGE_GIF && resultCode == RESULT_OK){

            val imageUri = data?.data
            filePath = imageUri

            val type = filePath?.let { contentResolver.getType(it) }
            Log.i("gif",type.toString())
            if (type=="image/gif") {

                var msg=MessageVO(System.currentTimeMillis().toString(),null,senderId,receiptUser.qrCode,null,null,"gif")
                imageUri?.let { mPresenter.onTapSendGifFile(it,msg) }
            }else{
                showError("Please choose gif file")
            }
        }



    }

    private fun hasPermission():Boolean{

        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
//      ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
//      ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,permissions,REQUEST_IMAGE_CAPTURE)
    }

    override fun showMessages(msg: List<MessageVO>) {
        mChatAdapter.setNewData(msg)
        rvChatMessage.scrollToPosition(msg.size)
    }

    override fun showError(error: String) {
      //  showError(error)
        Snackbar.make(window.decorView,error,Snackbar.LENGTH_SHORT).show()
    }

    private fun changeImageToBitmap(){
        try {

            filePath?.let {file->
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
}
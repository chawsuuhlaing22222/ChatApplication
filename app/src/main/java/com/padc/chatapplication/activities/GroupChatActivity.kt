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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.GroupChatAdapter
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.mvp.presenters.GroupChatPresenter
import com.padc.chatapplication.mvp.presenters.impls.GroupChatPresenterImpl
import com.padc.chatapplication.mvp.views.GroupChatView
import kotlinx.android.synthetic.main.activity_group_chat.*
import java.io.IOException

class GroupChatActivity : AppCompatActivity() ,GroupChatView{


    lateinit var mPresenter:GroupChatPresenter
    lateinit var mGroupVo:GroupVO
    lateinit var  mChatAdapter : GroupChatAdapter

    val REQUEST_IMAGE_CAPTURE=200
    private val REQUEST_IMAGE_GALLERY = 201
    private val REQUEST_IMAGE_GIF = 202
    val permissions= arrayOf(android.Manifest.permission.CAMERA)

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null

    companion object{

        const val IEXTRA_GROUP="GROUP"
        fun newIntent(context: Context,group:String):Intent{
            var intent=Intent(context,GroupChatActivity::class.java)
            intent.putExtra(IEXTRA_GROUP,group)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        setUpPresenter()
        setData()
        setUpRecycler()
        setUpActionListener()
        mPresenter.onUiReady(this,this)
        mPresenter.getGroupMsg(mGroupVo.createdTimeStamp.toString())

    }

    private fun setUpActionListener() {

        ivSendMsgInGroup.setOnClickListener {
            var msg=edtTypeMsgInGroupChat.text.toString()
            var timeStamp=System.currentTimeMillis().toString()
            var groupMsg=GroupMessageVO(mGroupVo.groupName,mGroupVo.groupCoverImg,timeStamp,
            mGroupVo.createdTimeStamp,msg,null,null,null)
            mPresenter.sendGroupChatMessage(mGroupVo,groupMsg)

            edtTypeMsgInGroupChat.setText("")
        }


        ivGalleryInGroup.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_IMAGE_GALLERY)
            // startActivity(intent)
        }

        ivCameraInGroup.setOnClickListener {
            if(hasPermission()){
                performCameraTakePhoto()

            }else{
                requestPermission()
            }
        }

        ivGifInGroup.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_IMAGE_GIF)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_IMAGE_CAPTURE ->{
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

    private fun performCameraTakePhoto() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "take_picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "take_picture_des")
        filePath = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
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
                var msg=edtTypeMsgInGroupChat.text.toString()
                var timeStamp=System.currentTimeMillis().toString()
                var groupMsg=GroupMessageVO(mGroupVo.groupName,mGroupVo.groupCoverImg,timeStamp,
                    mGroupVo.createdTimeStamp,msg,null,null,null,
                null,"image")

                bitmap?.let { mPresenter.uploadImageAndSendGroupMsg(it,mGroupVo,groupMsg) }
            }else{
                showError("Please choose image")
            }


        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            changeImageToBitmap()
            var msg=edtTypeMsgInGroupChat.text.toString()
            var timeStamp=System.currentTimeMillis().toString()
            var groupMsg=GroupMessageVO(mGroupVo.groupName,mGroupVo.groupCoverImg,timeStamp,
                mGroupVo.createdTimeStamp,msg,null,null,null,
                null,"image")

            bitmap?.let { mPresenter.uploadImageAndSendGroupMsg(it,mGroupVo,groupMsg) }
        }

        if(requestCode == REQUEST_IMAGE_GIF && resultCode == RESULT_OK){

            val imageUri = data?.data
            filePath = imageUri

            val type = filePath?.let { contentResolver.getType(it) }
            Log.i("gif",type.toString())
            if (type=="image/gif") {

                var msg=edtTypeMsgInGroupChat.text.toString()
                var timeStamp=System.currentTimeMillis().toString()

                var groupMsg=GroupMessageVO(mGroupVo.groupName,mGroupVo.groupCoverImg,timeStamp,
                    mGroupVo.createdTimeStamp,msg,null,null,null,
                null,"gif")
                imageUri?.let { mPresenter.uploadGifFileAndSendGroupMsg(it,mGroupVo,groupMsg) }
            }else{
                showError("Please choose gif file")
            }
        }



    }


    private fun setUpRecycler() {
        mChatAdapter = GroupChatAdapter()
        rvGroupChatMessage.apply {

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

            addOnLayoutChangeListener(object :View.OnLayoutChangeListener{
                override fun onLayoutChange(
                    p0: View?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                    p4: Int,
                    p5: Int,
                    p6: Int,
                    p7: Int,
                    p8: Int
                ) {
                    rvGroupChatMessage.scrollToPosition(rvGroupChatMessage.childCount-1)
                }

            })
        }
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[GroupChatPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setData() {
        var extra = intent.getStringExtra(IEXTRA_GROUP)
        var token = object : TypeToken<GroupVO>() {}.type
        var group = Gson().fromJson<GroupVO>(extra, token)
        mGroupVo=group
        //setdata
        Glide.with(this).load(group.groupCoverImg).into(ivPersonInGroupChatDetail)
        tvGroupNameInChatDetail.text = group.groupName
    }

    override fun showGroupChatMsg(msg: List<GroupMessageVO>) {
        mChatAdapter.setNewData(msg)


    }

    override fun showError(error: String) {
        Snackbar.make(window.decorView,error, Snackbar.LENGTH_SHORT).show()
    }

    private fun hasPermission():Boolean{

        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
//      ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
//      ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,permissions,REQUEST_IMAGE_CAPTURE)
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
package com.padc.chatapplication.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.AlphabetAdapter
import com.padc.chatapplication.adapters.NewGroupMemberAdapter
import com.padc.chatapplication.adapters.ParentContactPersonAdapter
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.ContactPresenter
import com.padc.chatapplication.mvp.presenters.impls.ContactPresenterImpl
import com.padc.chatapplication.mvp.views.ContactView
import kotlinx.android.synthetic.main.activity_new_group_create.*
import java.io.IOException

class NewGroupCreateActivity : AppCompatActivity() ,ContactView{

    lateinit var  parentContactPersonAdapter:ParentContactPersonAdapter
    lateinit var mPresenter: ContactPresenter
   lateinit var mNewMemberAdapter:NewGroupMemberAdapter
    private var mUserVO:UserVO?=null
    private var mContacts:Map<Char,List<UserVO>>?= mapOf()

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null

    companion object{
         var selectedUserList:MutableList<String> = mutableListOf()
        const val REQUEST_PICK_IMAGE=200
        const val IEXTRA_CONTACTS="contact"
        fun newIntent(context: Context,data:String):Intent{

            var intent=Intent(context,NewGroupCreateActivity::class.java)
            intent.putExtra(IEXTRA_CONTACTS,data)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group_create)

        setUpPresenter()
        setUpRecycler()
        setUpActionListener()

        //
        var type= object :TypeToken<Map<Char,List<UserVO>>>(){}.type
        var extra=intent.getStringExtra(IEXTRA_CONTACTS)
        var contacts=Gson().fromJson<Map<Char,List<UserVO>>>(extra, type)
        parentContactPersonAdapter.setNewData(contacts.toList())

        mPresenter.onUiReady(this,this)
    }

    private fun setUpActionListener() {
        ivGroupCoverImg.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_PICK_IMAGE)
        }

        btnCreateGroup.setOnClickListener {

           if(edtGroupName.text?.isNotBlank() == true && bitmap!=null){
              var groupName=edtGroupName.text.toString()

               var membersCount= selectedUserList.count()+1
               selectedUserList.add(mUserVO?.qrCode.toString())
               var member= selectedUserList.joinToString (",")

               bitmap?.let {
                   mPresenter.onTapToCreateNewGroup(
                       it,
                       GroupVO(groupName,member,null,System.currentTimeMillis().toString(),
                           null,membersCount))
               }

           }

            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            ivGroupCoverImg.setImageURI(imageUri)
            filePath = imageUri
            changeImageToBitmap()
        }
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

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ContactPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpRecycler() {
        //chat group
        mNewMemberAdapter = NewGroupMemberAdapter(mPresenter)
       // mNewMemberAdapter.setNewData(listOf(1,2,3,5))
        rvPersonAddedToCreateGroup.apply {
            adapter=mNewMemberAdapter
            layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.left=15
                }
            })
        }

        //contact group
        parentContactPersonAdapter = ParentContactPersonAdapter(mPresenter)
        parentContactPersonAdapter.setFlagCheck()
       // parentContactPersonAdapter.setNewData(listOf(1,2,3))
        rvChatPersonInNewGroup.apply {
            adapter=parentContactPersonAdapter
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

        //alphabet
        var alphabetAdapter = AlphabetAdapter()
        alphabetAdapter.setNewData(listOf("","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"))
        rvAlphabetInNewGroup.apply {
            adapter=alphabetAdapter
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
                    outRect.top=20
                }
            })
        }
    }


    override fun saveCurrentUserInfo(userVO: UserVO) {
        mUserVO = userVO
    }

    override fun showContacts(contacts:Map<Char,List<UserVO>>) {
        mContacts=contacts
        parentContactPersonAdapter.setNewData(contacts.toList())
    }

    override fun showPeerToPeerChat(receipt: UserVO) {
        var receiptJson=Gson().toJson(receipt)
        startActivity(PeerToPeerChatActivity.newIntent(this,receiptJson) )
    }

    override fun showGroups(groups: List<GroupVO>) {

    }

    override fun showSelectedGroupMember(users: List<UserVO>) {

        selectedUserList= users.map { user->user.qrCode } as MutableList<String>
       mNewMemberAdapter.setNewData(users)
    }

    override fun unselectUserId() {
        //selectedUserList.remove(userId)
       // parentContactPersonAdapter.setSelectedUserIds(selectedUserList)
        parentContactPersonAdapter.notifyDataSetChanged()
    }

    override fun showGroupChat(groupVO: GroupVO) {

    }

    override fun showError(error: String) {

    }
}
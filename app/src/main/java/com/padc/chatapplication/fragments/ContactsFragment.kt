package com.padc.chatapplication.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.padc.chatapplication.R
import com.padc.chatapplication.activities.GroupChatActivity
import com.padc.chatapplication.activities.NewGroupCreateActivity
import com.padc.chatapplication.activities.PeerToPeerChatActivity
import com.padc.chatapplication.activities.QRcodeScanner
import com.padc.chatapplication.adapters.AlphabetAdapter
import com.padc.chatapplication.adapters.ChatGroupAdapter
import com.padc.chatapplication.adapters.ParentContactPersonAdapter
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.ContactPresenter
import com.padc.chatapplication.mvp.presenters.impls.ContactPresenterImpl
import com.padc.chatapplication.mvp.views.ContactView
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : Fragment(),ContactView {

    lateinit var mChatAdapter:ChatGroupAdapter
    private var mUserVO:UserVO?=null
    lateinit var mPresenter: ContactPresenter
    lateinit var parentContactPersonAdapter:ParentContactPersonAdapter
    private var mContacts:Map<Char,List<UserVO>>?= mapOf()
    private var mSearchContactList:Map<Char,List<UserVO>>?= mapOf()
    private var mSearchGroupList:MutableList<GroupVO> = mutableListOf()
    private var mGroups:List<GroupVO> = listOf()
    var alphabetAdapter = AlphabetAdapter()
    var groupCountTxtView:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupCountTxtView=tvGroupCount
        setUpPresenter()
        setUpRecycler()

        setUpActionListener()
        context?.let { mPresenter.onUiReady(it,this) }


    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ContactPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpActionListener() {

        val barcodeLauncher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (!result.contents.isNullOrEmpty()) {
                Log.i("qr",result.contents)
                Toast.makeText(context, "New contact is added", Toast.LENGTH_SHORT).show()
                mPresenter.createContact(result.contents)
            }else{
                // CANCELED
            }
        }

        rlCreateNewGroup.setOnClickListener {

            var contacts=Gson().toJson(mContacts)
            startActivity(context?.let { it1 -> NewGroupCreateActivity.newIntent(it1,contacts) })
        }

        btnContact.setOnClickListener {

            val options = ScanOptions()
                .setOrientationLocked(false)
                .setCaptureActivity(QRcodeScanner::class.java)
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true)
                .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)

            barcodeLauncher.launch(options)
           // var userJson= Gson().toJson(mUserVO)
           // startActivity(context?.let { it1 -> QRcodeScanner.newIntent(it1,userJson) })
        }


        edtSearchName.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(edtSearchName.text?.trim().toString())
                return@OnEditorActionListener true
            }
            false
        })

        ivCancelForSearch.setOnClickListener {
            //contacts
            mContacts?.let { it1 -> parentContactPersonAdapter.setNewData(it1.toList().sortedBy { c ->c.first }) }

            //groups
            mGroups?.let {
                mChatAdapter.setNewData(it)
            }

            //clear text
            edtSearchName.setText("")

            //check empty view
            if(mContacts?.size==0  && mGroups.size==0){
                rlContactBody.visibility=View.GONE
                llEmptyContact.visibility=View.VISIBLE
            }else{
                rlContactBody.visibility=View.VISIBLE
                llEmptyContact.visibility=View.GONE
            }
        }
    }

    private fun performSearch(name: String) {

        //inital default empty
        mSearchGroupList = mutableListOf()
        mSearchContactList = mapOf()


        //search contacts
        var searchContactList:MutableList<UserVO> = mutableListOf()
        var firstChar=name.first().uppercaseChar()
        var contactsUsers=mContacts?.get(firstChar)
        contactsUsers?.let {
            it.forEach { user->
                if(user.name.contains(name,true)){
                    searchContactList.add(user)
                }
            }
        }

        //transform serach contact result to map
        searchContactList?.let {
            mSearchContactList = it.groupBy { it.name.first() }

        }

        //set search contact result to adapter
        mSearchContactList?.let {
            parentContactPersonAdapter.setNewData(it.toList())
        }

        //search groups
        mGroups.forEach {
            if(it.groupName?.contains(name,true) == true){
                mSearchGroupList.add(it)
            }
        }

        //set search group result to adapter
        mSearchGroupList?.let {
            mChatAdapter.setNewData(it)
        }

        //check to show empty image
        if(mSearchContactList?.size==0  && mSearchGroupList.size==0){
            rlContactBody.visibility=View.GONE
            llEmptyContact.visibility=View.VISIBLE
        }else{
            rlContactBody.visibility=View.VISIBLE
            llEmptyContact.visibility=View.GONE
        }


    }

    private fun setUpRecycler() {

        //chat group
            mChatAdapter = ChatGroupAdapter(mPresenter)
            //mChatAdapter.setNewData(listOf(1,2,3,5))
            rvChatGroup.apply {
                adapter=mChatAdapter
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
                        outRect.left=40
                    }
                })
            }

        //contact group
          parentContactPersonAdapter = ParentContactPersonAdapter(mPresenter)
       // parentContactPersonAdapter.setNewData(listOf(1,2,3))
        rvChatPerson.apply {
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


            //scroll listener
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(recyclerView.adapter?.itemCount !=0){
                        val lManager = recyclerView.layoutManager as LinearLayoutManager?
                        val firstElementPosition = lManager!!.findFirstVisibleItemPosition()
                        var firstChar:Char?=mContacts?.toList()?.get(firstElementPosition)?.first
                        firstChar?.let { alphabetAdapter.setCurrentChar(it) }
                    }

                }
            })
        }

        //alphabet

        alphabetAdapter.setNewData(listOf("","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"))
        rvAlphabet.apply {
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
         parentContactPersonAdapter.setNewData(contacts.toList().sortedBy { c ->c.first })
    }

    override fun showPeerToPeerChat(receipt: UserVO) {
        var receiptJson=Gson().toJson(receipt)
        startActivity(context?.let { PeerToPeerChatActivity.newIntent(it,receiptJson) })
    }

    override fun showGroups(groups: List<GroupVO>) {
        //var groupNameList:List<String>=groups.map { g->g.groupName }

        if(groups.size!=0){
            groupCountTxtView?.text="Groups(${groups.size})"
        }

        this.mGroups=groups
        mChatAdapter.setNewData(groups)
    }

    override fun showSelectedGroupMember(users: List<UserVO>) {

    }

    override fun unselectUserId() {

    }

    override fun showGroupChat(groupVO: GroupVO) {
        var groupJson=Gson().toJson(groupVO)
        startActivity(context?.let { GroupChatActivity.newIntent(it,groupJson) })
    }

    override fun showError(error: String) {
        showError(error)
    }


}
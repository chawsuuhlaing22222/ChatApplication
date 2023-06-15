package com.padc.chatapplication.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.padc.chatapplication.R
import com.padc.chatapplication.activities.CreateMomentActivity
import com.padc.chatapplication.adapters.MomentAdapter
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.mvp.presenters.impls.MomentPresenterImpl
import com.padc.chatapplication.mvp.views.MomentView
import com.padc.chatapplication.utils.ConfigUtils
import kotlinx.android.synthetic.main.fragment_moment.*


class MomentFragment : Fragment(),MomentView {

lateinit var mMomentAdapter: MomentAdapter
lateinit var mMomentPresenter: MomentPresenterImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpRecycler()
        setUpActionListener()
       // mMomentPresenter.getAllMoments()
        context?.let { mMomentPresenter.onUiReady(it,this) }
    }

    private fun setUpPresenter() {
        mMomentPresenter = ViewModelProvider(this)[MomentPresenterImpl::class.java]
        mMomentPresenter.initPresenter(this)
    }

    private fun setUpActionListener() {
        btnCreateMoment.setOnClickListener {
            startActivity(Intent(context,CreateMomentActivity::class.java))
        }
    }

    private fun setUpRecycler() {
        mMomentAdapter = MomentAdapter()
        rvMoments.apply {
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

    override fun saveImageUrl(img: String) {

    }

    override fun saveCurrentUserInfo(userVO: UserVO) {
       ConfigUtils.getInstance().saveUserName(userVO.name)
    }

    override fun showAllMoments(moments: List<MomentVO>) {
        if(!moments.isNullOrEmpty()){
                mMomentAdapter.setNewData(moments.sortedByDescending { moment->moment.uploadDate })
        }

    }

    override fun showError(error: String) {
        showError(error)
    }


}
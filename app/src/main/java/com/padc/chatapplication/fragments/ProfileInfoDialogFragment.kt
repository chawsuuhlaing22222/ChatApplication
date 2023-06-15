package com.padc.chatapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.CustomDropDownAdapter
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.dummy.mDays
import com.padc.chatapplication.dummy.mMonths
import com.padc.chatapplication.dummy.mYears
import com.padc.chatapplication.mvp.presenters.impls.MePresenterImpl
import kotlinx.android.synthetic.main.fragment_profile_info_dialog.*


class ProfileInfoDialogFragment : DialogFragment() {

    private var day:String?=null
    private var month:String?=null
    private var year:String?=null

    lateinit var mUserVO: UserVO
    lateinit var mPresenter: MePresenterImpl


    companion object{
        const val IEXTRA_USER="user_vo"
        fun newFragment(user:String): ProfileInfoDialogFragment {
            var frag=ProfileInfoDialogFragment()
            val mArgs = Bundle()
            mArgs.putString(IEXTRA_USER,user)
            frag.arguments=mArgs
            return frag
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_info_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setData()
        setUpActionListener()



    }

    private fun setUpPresenter() {
        activity?.let {
            mPresenter=ViewModelProvider(this)[MePresenterImpl::class.java]
        }
    }

    private fun setData() {
        var user=arguments?.getString(IEXTRA_USER)
        var token= object : TypeToken<UserVO>() {}.type
        mUserVO=Gson().fromJson<UserVO>(user,token)


        edtYourNameInDialog.setText(mUserVO.name.toString())
        edtPhNoInDialog.setText(mUserVO.phoneNo)

        var dob=mUserVO.dob?.split('-')
        tvSelectedYearInProfile.text=dob?.last()
        year=dob?.last()
        tvSelectedDaysInProfile.text=dob?.first()
        day=dob?.first()
        tvSelectedMonthInProfile.text=dob?.get(1)
        month=dob?.get(1)

       when(mUserVO.gender){
           "Female"->rbFemaleInProfile.isChecked=true
           "Male"->rbMaleInProfile.isChecked=true
           else->rbOtherInProfile.isChecked=true
       }
    }

    private fun setUpActionListener(){
        spinnerDaysInProfile.adapter = context?.let { CustomDropDownAdapter(it, mDays) }
        spinnerMonthInProfile.adapter= context?.let { CustomDropDownAdapter(it, mMonths) }
        spinnerYearInProfile.adapter = context?.let { CustomDropDownAdapter(it, mYears) }

        rlDaysInProfile.setOnClickListener {
            spinnerDaysInProfile.performClick()

        }

        rlMonthInProfile.setOnClickListener {
            spinnerMonthInProfile.performClick()
        }

        rlYearInProfile.setOnClickListener {
            spinnerYearInProfile.performClick()
        }

        spinnerDaysInProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedDaysInProfile.text= mDays[position].toString()
                    day= mDays[position].toString()
                }

            }

        }


        spinnerMonthInProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedMonthInProfile.text= mMonths[position].toString()
                    month= mMonths[position].toString()


                }

            }

        }

        spinnerYearInProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedYearInProfile.text= mYears[position].toString()
                    year= mYears[position].toString()
                }

            }

        }


        //btnSave
        btnSave.setOnClickListener {
            var  gender = when(rgGenderInProfile.checkedRadioButtonId){
                R.id.rbFemaleInProfile-> "Female"
                R.id.rbMaleInProfile-> "Male"
                else-> "Other"
            }
            var name=edtYourNameInDialog.text.toString()
            var dob="$day-$month-$year"
            var phno=edtPhNoInDialog.text.toString()
            var userVO=UserVO(name,mUserVO.email ?: "", phno,mUserVO.profileImage,mUserVO.qrCode,dob,mUserVO.password ?: "",gender)

            mPresenter.updateUserInfo(userVO)
           dismiss()


        }
    }





}
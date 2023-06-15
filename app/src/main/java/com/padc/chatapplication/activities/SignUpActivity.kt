package com.padc.chatapplication.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padc.chatapplication.R
import com.padc.chatapplication.adapters.CustomDropDownAdapter
import com.padc.chatapplication.data.vos.UserVO
import com.padc.chatapplication.dummy.mDays
import com.padc.chatapplication.dummy.mMonths
import com.padc.chatapplication.dummy.mYears
import com.padc.chatapplication.mvp.presenters.SignUpPresenter
import com.padc.chatapplication.mvp.presenters.impls.SignUpPresenterImpl
import com.padc.chatapplication.mvp.views.SignUpView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.view_holder_alert_to_login_dialog.view.*
import java.io.IOException

class SignUpActivity : AppCompatActivity(),SignUpView {

    private val REQUEST_PICK_IMAGE = 100

    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null
    lateinit var mPresenter:SignUpPresenter

    private var email:String?=null
    private var password:String?=null
    private var day:String?=null
    private var month:String?=null
    private var year:String?=null

    companion object{
        const val IEXTRA_EMAIL="email"
        const val IEXTRA_PASSWORD="password"
        fun newIntent(context: Context,email:String,password:String):Intent{
            var intent=Intent(context,SignUpActivity::class.java)
            intent.putExtra(IEXTRA_EMAIL,email)
            intent.putExtra(IEXTRA_PASSWORD,password)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email=intent.getStringExtra(IEXTRA_EMAIL)
        password = intent.getStringExtra(IEXTRA_PASSWORD)

        setUpPresenter()
        setUpListener()
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[SignUpPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpListener() {
        var dayAdapter = ArrayAdapter(
            this,
            R.layout.view_item_custom_spinner_dropdown,
            R.id.tvSpinnerSelectedValue,
            mDays
            //resources.getStringArray(R.array.days)
        )
        spinnerDays.adapter = CustomDropDownAdapter(this, mDays)
        spinnerMonth.adapter=CustomDropDownAdapter(this, mMonths)
        spinnerYear.adapter = CustomDropDownAdapter(this, mYears)

        rlDays.setOnClickListener {
            spinnerDays.performClick()

        }

        rlMonth.setOnClickListener {
            spinnerMonth.performClick()
        }

        rlYear.setOnClickListener {
            spinnerYear.performClick()
        }

        spinnerDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedDays.text= mDays[position].toString()
                    day=mDays[position].toString()
                }

            }

        }


        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedMonth.text= mMonths[position].toString()
                    month=mMonths[position].toString()


                }

            }

        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0){
                    tvSelectedYear.text= mYears[position].toString()
                    year= mYears[position].toString()
                }

            }

        }




        btnSignUp.setOnClickListener {

         var  gender = when(rgGender.checkedRadioButtonId){
                R.id.rbFemale-> "Female"
                R.id.rbMale-> "Male"
                else-> "Other"
            }
            var name=edtUserName.text.toString()
            var dob="$day-$month-$year"
            var phno=edtUserPhoneNo.text.toString()
            var userVO=UserVO(name,email ?: "", phno,null,null,dob,password ?: "",gender)
            bitmap?.let { it1 ->
                mPresenter.onTapSignUp(it1, userVO)
            }
        }

        ivProfile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_PICK_IMAGE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            ivProfile.setImageURI(imageUri)
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

    override fun showLogin() {
     var  dialog=AlertDialog.Builder(this)
        var view=LayoutInflater.from(this).inflate(R.layout.view_holder_alert_to_login_dialog,null)
        dialog.setView(view)
        dialog.show()
       view.btnOK.setOnClickListener {
          var intent=Intent(this,LoginActivity::class.java)
          intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
          startActivity(intent)
          finish()
      }
      //Snackbar.make(window.decorView,"user created successfully",Snackbar.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Snackbar.make(window.decorView,error,Snackbar.LENGTH_SHORT).show()
    }
}



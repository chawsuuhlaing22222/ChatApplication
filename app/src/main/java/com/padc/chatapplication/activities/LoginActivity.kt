package com.padc.chatapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.padc.chatapplication.R
import com.padc.chatapplication.mvp.presenters.LoginPresenter
import com.padc.chatapplication.mvp.presenters.impls.LoginPresenterImpl
import com.padc.chatapplication.mvp.views.LoginView
import com.padc.chatapplication.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() ,LoginView{
    lateinit var mPresenter:LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpPresenter()
        setUpActionListener()
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[LoginPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpActionListener() {

        btnLogin.setOnClickListener {
            var email=edtEmail.text.toString()
            var password=edtEnterYourPassword.text.toString()


           mPresenter.onTapLogin(email, password)
        }
    }

    private fun setUpToolbar() {
        //setSupportActionBar(toolbarLogin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            androidx.appcompat.R.id.home->{
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            else->{
                false
            }
        }
    }

    override fun showMainPage() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { ConfigUtils.getInstance().saveToken(it) }
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun showError(error: String) {
       Snackbar.make(window.decorView,error,Snackbar.LENGTH_SHORT).show()
    }
}
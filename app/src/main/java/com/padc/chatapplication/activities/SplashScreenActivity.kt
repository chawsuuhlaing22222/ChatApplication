package com.padc.chatapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.padc.chatapplication.R
import com.padc.chatapplication.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setUpActionListener()
        checkLoginUser()


    }

    private fun checkLoginUser() {
        if(!ConfigUtils.getInstance().getToken().isNullOrEmpty() && ConfigUtils.getInstance().getToken()!=""){
            var intent=Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun setUpActionListener() {

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, EmailSignUpActivity::class.java))
        }

    }
}
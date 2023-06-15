package com.padc.chatapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.padc.chatapplication.R
import kotlinx.android.synthetic.main.activity_otp_verify.*

class OtpVerifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)

        setUpActionListener()
    }

    private fun setUpActionListener() {
        btnVerify.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}
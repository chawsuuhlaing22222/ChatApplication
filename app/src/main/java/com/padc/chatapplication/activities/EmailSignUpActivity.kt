package com.padc.chatapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.padc.chatapplication.R
import kotlinx.android.synthetic.main.activity_email_sign_up.*

class EmailSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_up)

        setUpActionListener()
    }

    private fun setUpActionListener(){


        btnSignUpEmail.setOnClickListener {
            var email=edtEmail.text?.trim().toString()
            var password=edtEmailPassword.text.toString()
            startActivity(SignUpActivity.newIntent(this,email, password))
        }
    }
}
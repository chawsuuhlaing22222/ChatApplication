package com.padc.chatapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.padc.chatapplication.R
import com.padc.chatapplication.activities.SplashScreenActivity
import com.padc.chatapplication.utils.ConfigUtils
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogOut.setOnClickListener {
            ConfigUtils.getInstance().deleteToken()
            ConfigUtils.getInstance().deleteUserName()
            var intent= Intent(context, SplashScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


}
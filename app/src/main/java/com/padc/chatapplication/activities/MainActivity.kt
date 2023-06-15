package com.padc.chatapplication.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.padc.chatapplication.R
import com.padc.chatapplication.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchFragment(MomentFragment())

        setUpActionListener()
    }

    private fun setUpActionListener() {
        bottom_nav.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            return@setOnNavigationItemSelectedListener when (menuItem.itemId) {
                R.id.nav_moment -> {
                    switchFragment(MomentFragment())
                    true
                }
                R.id.nav_chat -> {
                    switchFragment(ChatFragment())
                    true
                }
                R.id.nav_contact -> {
                    switchFragment(ContactsFragment())
                    true
                }
                R.id.nav_me -> {
                    switchFragment(MeFragment())
                    true
                }
                R.id.nav_setting->{
                    switchFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment).commit()
    }

}
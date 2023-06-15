package com.padc.chatapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class ConfigUtils(context: Context) {

    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PRE, Context.MODE_PRIVATE)

    companion object {
        val SHARED_PRE = "ConfigUtils"
        val TOKEN = "Token"
        val USERNAME="Name"


        private var INSTANCE: ConfigUtils? = null
        fun getInstance(): ConfigUtils {
            if (INSTANCE == null) {
                throw RuntimeException("ConfigUtils is being invoked before initializing.")
            }
            val i = INSTANCE
            return i!!
        }

        fun initConfigUtils(context: Context) {
            INSTANCE = ConfigUtils(context)
        }
    }

    fun saveToken(token: String) {
        mSharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String {
        var token = mSharedPreferences.getString(TOKEN, "") ?: ""
        Log.i("token", token)
        return token
    }

    fun deleteToken(){
        mSharedPreferences.edit().remove(TOKEN
        ).apply()
    }

    fun saveUserName(username: String) {
        mSharedPreferences.edit().putString(USERNAME, username).apply()
    }

    fun getUserName(): String {
        var name = mSharedPreferences.getString(USERNAME, "") ?: ""
        Log.i("name", name)
        return name
    }

    fun deleteUserName(){
        mSharedPreferences.edit().remove(
            USERNAME
        ).apply()
    }
}
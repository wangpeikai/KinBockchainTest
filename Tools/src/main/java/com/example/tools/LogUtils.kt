package com.example.tools

import android.util.Log

object LogUtils {

    fun e(title: String? = this::class.java.name, msg: String?) {
        Log.e("${title}======>", msg ?: "")
    }

    fun log(message: String? = this::class.java.name, description: String?) {
        Log.e("${message}======>", description ?: "")
    }

}
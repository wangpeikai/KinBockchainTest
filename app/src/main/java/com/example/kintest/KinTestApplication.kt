package com.example.kintest

import android.app.Application

class KinTestApplication : Application() {

    companion object {
        private var instance: KinTestApplication? = null
        fun getInstance(): KinTestApplication {
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
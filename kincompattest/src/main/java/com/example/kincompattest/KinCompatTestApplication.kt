package com.example.kincompattest

import android.app.Application

class KinCompatTestApplication : Application() {

    companion object {
        private var instance: KinCompatTestApplication? = null
        fun getInstance(): KinCompatTestApplication {
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
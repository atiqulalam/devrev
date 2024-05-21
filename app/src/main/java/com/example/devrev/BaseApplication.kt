package com.example.devrev

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication:Application() {
    companion object{
        lateinit var application : BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}
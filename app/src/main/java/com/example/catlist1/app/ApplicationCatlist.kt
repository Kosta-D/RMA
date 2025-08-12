package com.example.catlist1.app

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationCatalist : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Test", "App:onCreate()")
    }
}
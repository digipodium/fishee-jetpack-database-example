package com.example.fishee

import android.app.Application
import com.example.fishee.data.local.AppDatabase

class FisheeApplication : Application() {

    val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
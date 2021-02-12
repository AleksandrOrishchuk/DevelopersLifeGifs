package com.ssho.orishchukfintechlab

import android.app.Application
import com.ssho.orishchukfintechlab.di.androidContext

class DevelopersLifeGifs : Application() {
    override fun onCreate() {
        super.onCreate()
        androidContext = applicationContext
    }
}
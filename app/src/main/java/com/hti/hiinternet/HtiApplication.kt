package com.hti.hiinternet

import android.app.Application
import android.content.Context
import com.marcinmoskala.kotlinpreferences.PreferenceHolder
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.DaggerAppCompatActivity

class HtiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceHolder.setContext(applicationContext)
    }

}
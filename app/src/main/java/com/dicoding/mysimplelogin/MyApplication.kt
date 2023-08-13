package com.dicoding.mysimplelogin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// `open` means other classes can inherit from this class & override its methods
open class MyApplication: Application() { // extend `Application` to maintain global application state`
    override fun onCreate() {
        super.onCreate()
        startKoin { // do injection to all child of "Application"
            androidContext(this@MyApplication) // provide `context` to all requesting function
            modules(storageModule) // add module to Koin
        }
    }
}
package com.diegobarrena.segundamano

import com.diegobarrena.segundamano.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {
    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }
}
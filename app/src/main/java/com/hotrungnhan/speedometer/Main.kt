package com.hotrungnhan.speedometer

import android.app.Application
import com.hotrungnhan.speedometer.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Main : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Main)
            modules(koinModule)
        }
    }
}
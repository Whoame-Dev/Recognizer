package ru.whoame.recogniser

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RecogniserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        startKoin {
            androidLogger()
            androidContext(this@RecogniserApplication)
            modules(appModules)
        }
    }

}

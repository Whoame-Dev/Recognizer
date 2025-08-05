package ru.whoame.recogniser

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class RecogniserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}

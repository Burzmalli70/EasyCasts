package dev.joewilliams.easycasts

import android.app.Application
import dev.joewilliams.easycasts.model.MainDatabase

class App: Application() {
    companion object {
        lateinit var sharedInstance: App
    }

    val database by lazy { MainDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        sharedInstance = this
    }
}
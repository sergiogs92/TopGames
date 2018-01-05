package com.sgsaez.topgames

import android.app.Application
import android.os.StrictMode
import com.sgsaez.topgames.di.components.ApplicationComponent
import com.sgsaez.topgames.di.components.DaggerApplicationComponent
import com.sgsaez.topgames.di.modules.ApplicationModule

class TopGamesApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        component.inject(this)
        ignoreURIExposure()
    }

    private fun initAppComponent() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    private fun ignoreURIExposure() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}
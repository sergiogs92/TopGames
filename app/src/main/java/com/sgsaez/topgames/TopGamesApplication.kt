package com.sgsaez.topgames

import android.app.Application
import com.sgsaez.topgames.di.components.ApplicationComponent
import com.sgsaez.topgames.di.components.DaggerApplicationComponent
import com.sgsaez.topgames.di.modules.ApplicationModule

class TopGamesApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        component.inject(this)
    }

    private fun initAppComponent() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}
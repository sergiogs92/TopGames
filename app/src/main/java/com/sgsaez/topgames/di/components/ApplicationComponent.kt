package com.sgsaez.topgames.di.components

import com.sgsaez.topgames.TopGamesApplication
import com.sgsaez.topgames.di.modules.ApplicationModule
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: TopGamesApplication)
    fun plus(gameListFragmentModule: GameListFragmentModule) : GameListFragmentComponent
    fun plus(gameDetailFragmentModule: GameDetailFragmentModule) : GameDetailFragmentComponent
}
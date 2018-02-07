package com.sgsaez.topgames.di.modules

import android.content.Context
import com.sgsaez.topgames.data.repositories.DefaultGameRepositoryMock
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.utils.AppSchedulerProvider
import com.sgsaez.topgames.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TopGamesApplicationModuleMock(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = applicationContext

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository = DefaultGameRepositoryMock()

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}
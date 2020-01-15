package com.sgsaez.topgames.di.modules

import android.content.Context
import com.sgsaez.topgames.data.repositories.DefaultGameRepositoryMock
import com.sgsaez.topgames.data.repositories.game.GameRepository
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

}
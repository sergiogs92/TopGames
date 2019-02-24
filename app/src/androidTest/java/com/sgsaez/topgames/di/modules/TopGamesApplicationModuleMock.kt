package com.sgsaez.topgames.di.modules

import android.content.Context
import com.sgsaez.topgames.data.repositories.DefaultFavouriteRepositoryMock
import com.sgsaez.topgames.data.repositories.DefaultGameRepositoryMock
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.support.AppSchedulerProvider
import com.sgsaez.topgames.support.SchedulerProvider
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
    fun provideFavouriteRepository(): FavouriteRepository = DefaultFavouriteRepositoryMock()

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}
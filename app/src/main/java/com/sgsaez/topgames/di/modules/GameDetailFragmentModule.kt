package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.AddFavourite
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.support.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class GameDetailFragmentModule {

    @Provides
    fun provideAddFavourites(favouriteRepository: FavouriteRepository, schedulerProvider: SchedulerProvider) =
            AddFavourite(favouriteRepository, schedulerProvider)

    @Provides
    fun providePresenter(addFavourite: AddFavourite) = GameDetailPresenter(addFavourite)

}
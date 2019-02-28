package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.support.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class FavouriteListFragmentModule {

    @Provides
    fun provideGetFavourites(favouriteRepository: FavouriteRepository, schedulerProvider: SchedulerProvider) =
            GetFavourites(favouriteRepository, schedulerProvider)

    @Provides
    fun provideRemoveFavourites(favouriteRepository: FavouriteRepository, schedulerProvider: SchedulerProvider) =
            RemoveFavourite(favouriteRepository, schedulerProvider)

    @Provides
    fun providePresenter(getFavourites: GetFavourites, removeFavourite: RemoveFavourite) =
            FavouriteListPresenter(getFavourites, removeFavourite)

}
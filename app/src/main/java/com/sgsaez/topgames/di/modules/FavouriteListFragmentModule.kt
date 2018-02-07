package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.utils.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class FavouriteListFragmentModule {

    @Provides
    fun provideGetFavourites(favouriteRepository: FavouriteRepository) = GetFavourites(favouriteRepository)

    @Provides
    fun provideRemoveFavourites(favouriteRepository: FavouriteRepository) = RemoveFavourite(favouriteRepository)

    @Provides
    fun providePresenter(getFavourites: GetFavourites, removeFavourite: RemoveFavourite, schedulerProvider: SchedulerProvider) =
            FavouriteListPresenter(getFavourites, removeFavourite, schedulerProvider)

}
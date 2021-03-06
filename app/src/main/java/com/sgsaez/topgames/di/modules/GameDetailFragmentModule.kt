package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.AddFavourite
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class GameDetailFragmentModule {

    @Provides
    fun provideAddFavourites(favouriteRepository: FavouriteRepository) = AddFavourite(favouriteRepository)

    @Provides
    fun providePresenter(addFavourite: AddFavourite) = GameDetailPresenter(addFavourite)

}
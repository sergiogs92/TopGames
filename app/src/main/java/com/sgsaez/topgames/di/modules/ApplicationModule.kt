package com.sgsaez.topgames.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.sgsaez.topgames.TopGamesApplication
import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.network.connectivity.DeviceConnectivity
import com.sgsaez.topgames.data.persistence.TopGamesDatabase
import com.sgsaez.topgames.data.repositories.favourite.DefaultFavouriteRepository
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.data.repositories.game.DefaultGameRepository
import com.sgsaez.topgames.data.repositories.game.GameRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(val application: TopGamesApplication) {

    private val BASE_URL = "https://www.giantbomb.com/api/games/"
    private val DATABASE_NAME = "topGameDB"

    @Provides
    @Singleton
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideGameRepository(retrofit: Retrofit, database: TopGamesDatabase, connectivityChecker: ConnectivityChecker): GameRepository {
        return DefaultGameRepository(
                retrofit.create(ApiService::class.java),
                database.gameDao(),
                connectivityChecker)
    }

    @Provides
    @Singleton
    fun provideFavouriteRepository(database: TopGamesDatabase): FavouriteRepository {
        return DefaultFavouriteRepository(database.favouriteDao())
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context)
            = Room.databaseBuilder(context, TopGamesDatabase::class.java, DATABASE_NAME).build()


    @Provides
    @Singleton
    fun provideConnectivityChecker(context: Context): ConnectivityChecker = DeviceConnectivity(context)

}
package com.sgsaez.topgames.di.modules

import android.content.Context
import com.google.gson.Gson
import com.sgsaez.topgames.TopGamesApplication
import com.sgsaez.topgames.data.repositories.DefaultGameRepository
import com.sgsaez.topgames.data.repositories.GameRepository
import com.sgsaez.topgames.data.service.GameService
import com.sgsaez.topgames.utils.AppSchedulerProvider
import com.sgsaez.topgames.utils.ConnectivityChecker
import com.sgsaez.topgames.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(val application: TopGamesApplication) {

    private val BASE_URL = "https://www.giantbomb.com/api/games/"

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
    fun provideGameRepository(retrofit: Retrofit, connectivityChecker: ConnectivityChecker): GameRepository {
        return DefaultGameRepository(
                retrofit.create(GameService::class.java),
                connectivityChecker)
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    @Singleton
    fun provideConnectionHelper(context: Context) = ConnectivityChecker(context)
}
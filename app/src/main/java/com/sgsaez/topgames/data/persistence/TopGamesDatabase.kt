package com.sgsaez.topgames.data.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.Game
import com.sgsaez.topgames.data.persistence.typeconverters.GameConverter

@Database(entities = [Game::class, Favourite::class], version = 1)
@TypeConverters(GameConverter::class)
abstract class TopGamesDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun favouriteDao(): FavouriteDao
}
package com.sgsaez.topgames.data.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.EGame
import com.sgsaez.topgames.data.persistence.typeconverters.GameConverter

@Database(entities = arrayOf(EGame::class), version = 1)
@TypeConverters(GameConverter::class)
abstract class TopGamesDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
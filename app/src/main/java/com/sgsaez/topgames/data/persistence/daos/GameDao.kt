package com.sgsaez.topgames.data.persistence.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sgsaez.topgames.data.persistence.entities.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getGames(): List<Game>

    @Query("SELECT * FROM game WHERE name LIKE :arg0")
    fun searchGames(search: String): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(games: List<Game>)
}

package com.sgsaez.topgames.data.persistence.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sgsaez.topgames.data.persistence.entities.EGame

@Dao
interface GameDao {
    @Query("SELECT * FROM egame ORDER BY name")
    fun getGames(): List<EGame>

    @Query("SELECT * FROM egame WHERE name LIKE :arg0")
    fun searchGames(search: String): List<EGame>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(games: List<EGame>)
}

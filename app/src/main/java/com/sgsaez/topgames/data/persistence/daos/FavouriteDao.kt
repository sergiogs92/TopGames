package com.sgsaez.topgames.data.persistence.daos

import android.arch.persistence.room.*
import com.sgsaez.topgames.data.persistence.entities.Favourite

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite ORDER BY name")
    fun getFavourites(): List<Favourite>

    @Query("SELECT * FROM favourite WHERE id=:arg0")
    fun getFavourite(id: String): Favourite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(favourite: Favourite)

    @Delete
    fun deleteFavourite(favourite: Favourite)
}
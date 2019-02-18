package com.sgsaez.topgames.data.persistence.daos

import android.arch.persistence.room.*
import com.sgsaez.topgames.data.persistence.entities.Favourite

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite")
    fun getFavourites(): List<Favourite>

    @Query("SELECT * FROM favourite WHERE id=:id")
    fun getFavourite(id: String): Favourite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(favourite: Favourite)

    @Delete
    fun deleteFavourite(favourite: Favourite)
}

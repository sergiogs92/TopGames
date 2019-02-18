package com.sgsaez.topgames.data.persistence.typeconverters

import android.arch.persistence.room.TypeConverter
import com.sgsaez.topgames.data.persistence.entities.Image

class GameConverter {

    @TypeConverter
    fun fromImage(image: Image): String = if(image.url.isEmpty()) "" else image.url

    @TypeConverter
    fun toImage(url: String): Image = Image(url)
}
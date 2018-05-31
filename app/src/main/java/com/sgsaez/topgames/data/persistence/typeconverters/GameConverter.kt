package com.sgsaez.topgames.data.persistence.typeconverters

import android.arch.persistence.room.TypeConverter
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.utils.condition

class GameConverter {

    @TypeConverter
    fun fromImage(image: Image): String {
        return condition({ image.url.isEmpty() }, { "" }, { image.url })
    }

    @TypeConverter
    fun toImage(url: String): Image {
        return Image(url)
    }
}
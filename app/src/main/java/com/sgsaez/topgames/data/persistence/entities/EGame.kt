package com.sgsaez.topgames.data.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sgsaez.topgames.presentation.model.Image

@Entity
data class EGame(@PrimaryKey var id: String,
                 var description: String?,
                 var name: String,
                 var image: Image) {

    constructor() : this("", "", "", Image(""))
}
package com.sgsaez.topgames.data.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Favourite(
        @PrimaryKey var id: String,
        var name: String,
        var description: String,
        var image: Image) {

    constructor() : this("", "", "", Image(""))
}
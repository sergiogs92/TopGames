package com.sgsaez.topgames.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(val id: String = "",
                val description: String = "",
                val name: String = "",
                val imageUrl: String = "") : Parcelable

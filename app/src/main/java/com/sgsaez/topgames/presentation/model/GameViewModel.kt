package com.sgsaez.topgames.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameViewModel(var id: String, var description: String, var name: String, var imageUrl: String) : Parcelable

package com.sgsaez.topgames.presentation.view

interface GameDetailView {
    fun addTitle(name: String)
    fun addDescription(content: String)
    fun addImage(url: String)
}
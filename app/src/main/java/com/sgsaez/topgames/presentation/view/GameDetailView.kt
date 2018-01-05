package com.sgsaez.topgames.presentation.view

interface GameDetailView {
    fun addTitleToolbar(name:String)
    fun addDescription(content: String)
    fun addImage(url: String)
}
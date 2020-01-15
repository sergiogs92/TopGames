package com.sgsaez.topgames.support.domains

import com.sgsaez.topgames.presentation.model.Game

data class Page<T>(
        val requestedPage: Int = 0,
        val items: List<T> = emptyList(),
        val query: String = ""
)

fun <T> Page<T>.update(other: Page<T>): Page<T> {
    return Page(
            requestedPage = if(other.requestedPage != 0) other.requestedPage else this.requestedPage,
            items = this.items + other.items,
            query = if(other.query.isNotEmpty()) other.query else this.query
    )
}

fun <T> Page<T>.isUpdateItems(data: List<Game>): Boolean {
    return this.items.size != data.size
}
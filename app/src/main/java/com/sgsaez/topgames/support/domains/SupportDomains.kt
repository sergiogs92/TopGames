package com.sgsaez.topgames.support.domains

data class Page<T>(
        val requestedPage: Int = 0,
        val items: List<T> = emptyList(),
        val query: String = ""
)

fun <T> Page<T>.update(other: Page<T>): Page<T> {
    return Page(
            requestedPage = other.requestedPage,
            items = this.items + other.items,
            query = other.query
    )
}
package com.sgsaez.topgames.presentation.presenters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.isNougatOrAbove

class GameDetailPresenter(private val context: Context) : BasePresenter<GameDetailView>() {

    fun paintDetail(game: Game) {
        view?.addTitle(game.name)
        view?.addDescription(convertHtmlText(game.description))
        view?.addImage(game.image.url)
    }

    @SuppressLint("NewApi")
    private fun convertHtmlText(description: String?): String {
        var detail: String = description ?: context.getString(R.string.noDescription)
        isNougatOrAbove {
            detail = Html.fromHtml(description ?: context.getString(R.string.noDescription), 0).toString()
        }
        return detail
    }
}
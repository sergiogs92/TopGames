package com.sgsaez.topgames.presentation.presenters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.text.Html
import android.widget.ImageView
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.isNougatOrAbove
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GameDetailPresenter(private val context: Context) : BasePresenter<GameDetailView>() {

    private val TYPE_TEXT = "text/plain"
    private val TYPE_IMAGE = "image/*"

    fun paintDetail(game: Game) {
        view?.addTitleToolbar(game.name)
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

    fun setUpIntentShared(game: Game, image: ImageView) {
        val textShared = game.name
        val bmpUri = getLocalBitmapUri(image)
        val shareIntent = Intent()
        shareIntent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textShared)
            type = TYPE_TEXT
            if (bmpUri != null) {
                putExtra(Intent.EXTRA_STREAM, bmpUri)
                type = TYPE_IMAGE
            }
        }
        context.startActivity(shareIntent)
    }

    private fun getLocalBitmapUri(imageView: ImageView): Uri? {
        val drawable = imageView.drawable
        val bmp: Bitmap?
        if (drawable is BitmapDrawable) bmp = (imageView.drawable as BitmapDrawable).bitmap else return null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getString(R.string.default_image_name))
            val out = FileOutputStream(file)
            bmp!!.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            return Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
package com.sgsaez.topgames.presentation.view.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.NotParcelled.fromNotParcelled
import com.sgsaez.topgames.utils.isLollipopOrAbove
import com.sgsaez.topgames.utils.loadUrl
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.description_item.*
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GameDetailFragment : Fragment(), GameDetailView {

    private val TYPE_TEXT = "text/plain"
    private val TYPE_IMAGE = "image/*"

    private val presenter: GameDetailPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(GameDetailFragmentModule()) }

    companion object {
        private const val GAME_KEY: String = "GAME_KEY"
        fun newInstance(game: String): GameDetailFragment {
            val fragment = GameDetailFragment()
            val args = Bundle()
            args.putString(GAME_KEY, game)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_game_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        val game = fromNotParcelled(arguments.getString(GAME_KEY), Game::class.java)
        initFloatingButton(game)
        presenter.attachView(this)
        presenter.onInit(context.getString(R.string.noDescription), game)
    }

    private fun initToolbar() {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { activity.onBackPressed() }
        }
    }

    private fun initFloatingButton(game: Game) {
        isLollipopOrAbove {
            floatingButton.apply {
                visibility = View.VISIBLE
                setOnClickListener { presenter.onSocialSharedClicked(game) }
            }
        }
    }

    override fun addTitleToolbar(name: String) {
        toolbar.title = name
    }

    override fun addDescription(content: String) {
        description.text = content
    }

    override fun addImage(url: String) {
        image.loadUrl(url)
    }

    override fun showSocialSharedNetworks(game: Game) {
        val textShared = game.name
        val bmpUri = getLocalBitmapUri()
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
        startActivity(shareIntent)
    }

    private fun getLocalBitmapUri(): Uri? {
        val drawable = image.drawable
        val bmp: Bitmap?
        if (drawable is BitmapDrawable) bmp = (image.drawable as BitmapDrawable).bitmap else return null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getString(R.string.default_image_name))
            val out = FileOutputStream(file)
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            return Uri.fromFile(file)
        } catch (ignored: IOException) {
        }
        return null
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}


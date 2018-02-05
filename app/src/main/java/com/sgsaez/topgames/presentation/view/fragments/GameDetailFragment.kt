package com.sgsaez.topgames.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.BuildConfig
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.isLollipopOrAbove
import com.sgsaez.topgames.utils.topGamesApplication
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.description_item.*
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.io.File
import java.io.FileOutputStream

class GameDetailFragment : Fragment(), GameDetailView {

    private val presenter: GameDetailPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(GameDetailFragmentModule()) }

    companion object {
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
        private const val GAME_KEY: String = "GAME_KEY"
        fun newInstance(game: GameViewModel): GameDetailFragment {
            val fragment = GameDetailFragment()
            val args = Bundle()
            args.putParcelable(GAME_KEY, game)
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
        val game = arguments.getParcelable<GameViewModel>(GAME_KEY)
        initFloatingButton(game)
        presenter.attachView(this)
        presenter.onInit(game)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        appBarLayout.layoutParams.apply {
            height = resources.displayMetrics.heightPixels / 2
        }
    }

    private fun initToolbar() {
        detailToolbar.apply {
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { activity.onBackPressed() }
        }
    }

    private fun initFloatingButton(game: GameViewModel) {
        isLollipopOrAbove {
            floatingButton.apply {
                setOnClickListener { presenter.onSocialSharedClicked(game) }
            }
        }
    }

    override fun addTitleToolbar(name: String) {
        detailToolbar.title = name
    }

    override fun addDescription(content: String) {
        description.text = content
    }

    override fun addImage(url: String) {
        Picasso.with(image.context).load(url).into(image, object : Callback {
            override fun onSuccess() {
                val bitmap = (image.drawable as BitmapDrawable).bitmap
                Palette.from(bitmap).generate { palette -> applyPalette(palette) }
            }

            override fun onError() {}
        })
    }

    @SuppressLint("NewApi")
    private fun applyPalette(palette: Palette) {
        if (activity != null) {
            val primaryDarkColor = ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
            val primaryColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
            collapsingToolbar.setContentScrimColor(palette.getMutedColor(primaryColor))
            isLollipopOrAbove {
                activity!!.window.statusBarColor = palette.getDarkMutedColor(primaryDarkColor)
            }
            updateBackground(palette, primaryColor, primaryDarkColor)
        }
    }

    private fun updateBackground(palette: Palette, primaryColor: Int, primaryDarkColor: Int) {
        val lightVibrantColor = palette.getLightVibrantColor(primaryDarkColor)
        val vibrantColor = palette.getVibrantColor(primaryColor)
        floatingButton.rippleColor = lightVibrantColor
        floatingButton.backgroundTintList = ColorStateList.valueOf(vibrantColor)
        floatingButton.show()
    }

    override fun showSocialSharedNetworks(game: GameViewModel) {
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
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getString(R.string.default_image_name))
        FileOutputStream(file).use {
            val bmp = (image.drawable as BitmapDrawable).bitmap
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    }

    override fun onDestroyView() {
        presenter.onResetStatusBarColor()
        presenter.detachView()
        super.onDestroyView()
    }

    @SuppressLint("NewApi")
    override fun resetStatusBarColor() {
        isLollipopOrAbove {
            activity.window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
    }
}


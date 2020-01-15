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
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.palette.graphics.Palette
import com.sgsaez.topgames.BuildConfig
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.viewmodel.GameDetailViewModel
import com.sgsaez.topgames.support.inflate
import com.sgsaez.topgames.support.topGamesApplication
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.description_item.*
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.io.File
import java.io.FileOutputStream

private const val TYPE_TEXT = "text/plain"
private const val TYPE_IMAGE = "image/*"
private const val GAME_KEY: String = "GAME_KEY"

fun gameDetailBundle(game: Game): Bundle = Bundle().apply { putParcelable(GAME_KEY, game) }

class GameDetailFragment : Fragment() {

    private val viewModelFactory: ViewModelProvider.Factory by lazy { component.factory() }
    private val component by lazy { topGamesApplication.component.plus(GameDetailFragmentModule()) }

    private lateinit var viewModel: GameDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameDetailViewModel::class.java)
        viewModel.state.observe(this, Observer {
            it?.let { state ->
                renderGame(state.game)
            }
        })
    }

    private fun renderGame(game: Game) {
        detailToolbar.title = game.name
        description.text = game.description
        renderImage(game.imageUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_game_detail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game = arguments?.getParcelable<Game>(GAME_KEY)!!
        initToolbar()
        initFloatingButton(game)
        viewModel.showGame(game)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        appBarLayout.layoutParams.apply {
            height = resources.displayMetrics.heightPixels / 2
        }
    }

    private fun initToolbar() {
        detailToolbar.apply {
            inflateMenu(R.menu.game_detail_menu)
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { view?.let { findNavController(it).popBackStack() } }
        }
    }

    private fun initFloatingButton(game: Game) {
        floatingButton.setOnClickListener { showSocialSharedNetworks(game) }
    }

    private fun renderImage(url: String) {
        Picasso.with(image.context).load(url).into(image, object : Callback {
            override fun onSuccess() {
                val bitmap = (image.drawable as BitmapDrawable).bitmap
                Palette.from(bitmap).generate { palette -> applyPalette(palette!!) }
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
            activity!!.window.statusBarColor = palette.getDarkMutedColor(primaryDarkColor)
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

    private fun showSocialSharedNetworks(game: Game) {
        val textShared = game.name
        val bmpUri = getLocalBitmapUri()
        val shareIntent = Intent()
        shareIntent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textShared)
            type = TYPE_TEXT
            bmpUri?.let {
                putExtra(Intent.EXTRA_STREAM, bmpUri)
                type = TYPE_IMAGE
            }
        }
        startActivity(shareIntent)
    }

    private fun getLocalBitmapUri(): Uri? {
        val file = File(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context!!.getString(R.string.default_image_name))
        FileOutputStream(file).use {
            val bmp = (image.drawable as BitmapDrawable).bitmap
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
            return FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", file)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetStatusBarColor()
    }

    @SuppressLint("NewApi")
    private fun resetStatusBarColor() {
        activity!!.window.statusBarColor = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
    }

}
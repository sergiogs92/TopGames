package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.FavouriteListFragmentModule
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.presentation.view.renderers.FavouriteListRenderer
import com.sgsaez.topgames.support.navigation.navigateTo
import com.sgsaez.topgames.support.topGamesApplication
import kotlinx.android.synthetic.main.fragment_favourite_list.*

fun newFavouriteListInstance() = FavouriteListFragment()

class FavouriteListFragment : Fragment(), FavouriteListView {

    private val presenter: FavouriteListPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(FavouriteListFragmentModule()) }
    private lateinit var renderer: FavouriteListRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        presenter.initJob()
        initToolbar()
        initRenderer()
        paintGames()
    }

    private fun initToolbar() {
        listToolbar.apply {
            title = resources.getString(R.string.favourites)
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { activity!!.onBackPressed() }
        }
    }

    private fun initRenderer() {
        renderer = FavouriteListRenderer(recyclerView, object : FavouriteListRenderer.FavouriteListener {
            override fun onClickInGame(favouriteGame: GameViewModel) {
                presenter.onFavouriteClicked(favouriteGame)
            }

            override fun onClickInClose(favouriteGame: GameViewModel) {
                presenter.onRemoveFavouriteGame(favouriteGame)
            }

        })
    }

    private fun paintGames(){
        presenter.onLoadFavourites()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        initRenderer()
    }

    override fun addFavouriteToList(favouriteGames: List<GameViewModel>) {
        renderer.render(favouriteGames)
    }

    override fun removeFavouriteToList(favouriteGame: GameViewModel) {
        renderer.removeFavourite(favouriteGame)
    }

    override fun showNoDataFoundError() {
        Toast.makeText(context, resources.getString(R.string.error_no_data_found), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToGame(favouriteGame: GameViewModel) {
        val detailsFragment = newGameDetailInstance(favouriteGame, true)
        activity!!.navigateTo(detailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onDestroy() {
        presenter.cancelJob()
        super.onDestroy()
    }

}
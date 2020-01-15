package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.FavouriteListFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.presentation.view.renderers.FavouriteListRenderer
import com.sgsaez.topgames.support.inflate

fun newFavouriteListInstance() = FavouriteListFragment()

class FavouriteListFragment : Fragment(), FavouriteListView {

    private val presenter: FavouriteListPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(FavouriteListFragmentModule()) }
    private lateinit var renderer: FavouriteListRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_favourite_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        presenter.initJob()
        initToolbar()
        initRenderer()
        presenter.onLoadFavourites()
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
            override fun onClickInGame(favouriteGame: Game) {
                presenter.onFavouriteClicked(favouriteGame)
            }

            override fun onClickInClose(favouriteGame: Game) {
                presenter.onRemoveFavouriteGame(favouriteGame)
            }

        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        initRenderer()
        presenter.onLoadFavourites()
    }

    override fun addFavouriteToList(favouriteGames: List<Game>) {
        renderer.render(favouriteGames)
    }

    override fun removeFavouriteToList(favouriteGame: Game) {
        renderer.removeFavourite(favouriteGame)
    }

    override fun showNoDataFoundError() {
        Toast.makeText(context, resources.getString(R.string.error_no_data_found), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToGame(favouriteGame: Game) {
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
package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.FavouriteListFragmentModule
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.presentation.view.adapters.FavouriteListAdapter
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.fragment_favourite_list.*

class FavouriteListFragment : Fragment(), FavouriteListView {

    private val presenter: FavouriteListPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(FavouriteListFragmentModule()) }
    private val favouriteListAdapter by lazy {
        FavouriteListAdapter(mutableListOf(), object : FavouriteListAdapter.FavouriteListener {
            override fun onClickInGame(favouriteGame: GameViewModel) {
                presenter.onFavouriteClicked(favouriteGame)
            }

            override fun onClickInClose(favouriteGame: GameViewModel) {
                presenter.onRemoveFavouriteGame(favouriteGame)
            }
        })
    }

    companion object {
        fun newInstance(): FavouriteListFragment {
            return FavouriteListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_favourite_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        presenter.attachView(this)
        if (favouriteListAdapter.itemCount == 0) {
            presenter.onLoadFavourites()
        }
    }

    private fun initToolbar() {
        listToolbar.apply {
            title = resources.getString(R.string.favourites)
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { activity.onBackPressed() }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        initAdapter()
    }

    private fun initAdapter() = recyclerView.apply {
        setHasFixedSize(true)
        val orientation = resources.configuration.orientation
        layoutManager = GridLayoutManager(context, if (orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5)
        adapter = favouriteListAdapter
    }

    override fun addFavouriteToList(favouriteGames: List<GameViewModel>) {
        val adapter = recyclerView.adapter as FavouriteListAdapter
        adapter.addFavourites(favouriteGames)
    }

    override fun removeFavouriteToList(favouriteGame: GameViewModel) {
        favouriteListAdapter.removeFavourite(favouriteGame)
    }

    override fun showNoDataFoundError() {
        Toast.makeText(context, resources.getString(R.string.error_no_data_found), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToGame(favouriteGame: GameViewModel) {
        val detailsFragment = GameDetailFragment.newInstance(favouriteGame, true)
        (activity as MainActivity).addFragment(detailsFragment)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
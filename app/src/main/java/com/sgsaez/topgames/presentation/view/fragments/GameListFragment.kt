package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.presentation.view.adapters.GameListAdapter
import com.sgsaez.topgames.utils.NotParcelled.toNotParcelled
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.fragment_game_list.*

class GameListFragment : Fragment(), GameListView {

    private val presenter: GameListPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(GameListFragmentModule()) }
    private val gameListAdapter by lazy {
        val gameList = mutableListOf<Game>()
        GameListAdapter(gameList) { game, _ -> navigateToDetailFragment(game) }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeLayout()
        initAdapter()
        presenter.attachView(this)
        if (gameListAdapter.itemCount == 0) {
            showLoading()
            presenter.getGames(true)
        }
    }

    private fun initSwipeLayout() = swipeRefreshLayout.setOnRefreshListener({ presenter.getGames(isRefresh = true) })

    private fun initAdapter() {
        recyclerView.apply {
            setHasFixedSize(true)
            val orientation = resources.configuration.orientation
            layoutManager = GridLayoutManager(context, if (orientation == ORIENTATION_PORTRAIT) 2 else 3)
            adapter = gameListAdapter
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun addGameToList(games: List<Game>) {
        val adapter = recyclerView.adapter as GameListAdapter
        adapter.addGames(games)
    }

    override fun showEmptyListError() {
        errorView.visibility = View.VISIBLE
    }

    override fun hideEmptyListError() {
        errorView.visibility = View.GONE
    }

    override fun showToastError(message: String?) {
        val errorMessage = message ?: getString(R.string.errorLoadingMessage)
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun clearList() {
        gameListAdapter.clearGames()
    }

    private fun navigateToDetailFragment(game: Game) {
        val detailsFragment = GameDetailFragment.newInstance(toNotParcelled(game))
        (activity as MainActivity).addDetailFragment(detailsFragment)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

}
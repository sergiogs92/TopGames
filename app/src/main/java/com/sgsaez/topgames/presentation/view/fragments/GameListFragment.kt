package com.sgsaez.topgames.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.renderers.GameListRenderer
import com.sgsaez.topgames.presentation.viewmodel.GameListError
import com.sgsaez.topgames.presentation.viewmodel.GameListViewModel
import com.sgsaez.topgames.support.domains.Page
import com.sgsaez.topgames.support.inflate
import kotlinx.android.synthetic.main.fragment_game_list.*

class GameListFragment : Fragment() {

    private val viewModelFactory: ViewModelProvider.Factory by lazy { component.factory() }
    private val component by lazy { topGamesApplication.component.plus(GameListFragmentModule()) }

    private lateinit var viewModel: GameListViewModel
    private lateinit var renderer: GameListRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameListViewModel::class.java)
        viewModel.state.observe(this, Observer {
            it?.let { state ->
                renderLoading(state.isLoading)
                renderGames(state.page)
                renderError(state.error)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_game_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSwipeLayout()
        initRenderer()
        viewModel.showGames()
    }

    private fun initToolbar() {
        listToolbar.title = resources.getString(R.string.app_name)
    }

    private fun initSwipeLayout() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent)
            setOnRefreshListener { viewModel.showGames(isRefresh = true) }
        }
    }

    private fun initRenderer() {
        renderer = GameListRenderer(recyclerView, object : GameListRenderer.GameListener {
            override fun onLoadMore(requestPage: Int) = viewModel.onLoadMore(requestPage)

            override fun onClickInGame(game: Game) {
                view?.let {
                    findNavController(it).navigate(R.id.action_to_game_detail, gameDetailBundle(game))
                }
            }

        })
    }

    private fun renderLoading(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun renderGames(page: Page<Game>) = renderer.render(page)

    private fun renderError(gameListError: GameListError) {
        val error = when (gameListError) {
            GameListError.INTERNET_CONNECTION_ERROR -> resources.getString(R.string.error_internet_connection)
            GameListError.NO_DATA_FOUND -> resources.getString(R.string.error_no_data_found)
            GameListError.DEFAULT_ERROR -> resources.getString(R.string.error_default)
            GameListError.NONE -> null
        }
        error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

}
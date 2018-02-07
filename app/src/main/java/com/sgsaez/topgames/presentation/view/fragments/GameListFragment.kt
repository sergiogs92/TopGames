package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.presentation.view.adapters.GameListAdapter
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.fragment_game_list.*

class GameListFragment : Fragment(), GameListView {

    private val presenter: GameListPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(GameListFragmentModule()) }
    private var query: String = ""
    private val gameListAdapter by lazy {
        GameListAdapter(mutableListOf(), object : GameListAdapter.GameListener {
            override fun onClickInGame(game: GameViewModel) {
                presenter.onGameClicked(game)
            }
        })
    }

    companion object {
        private const val QUERY_KEY: String = "QUERY_KEY"
        fun newInstance(query: String): GameListFragment {
            return GameListFragment().apply {
                val args = Bundle()
                args.putString(QUERY_KEY, query)
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        query = arguments.getString(QUERY_KEY)
        initToolbar()
        initSwipeLayout()
        initAdapter()
        presenter.attachView(this)
        if (gameListAdapter.itemCount == 0) {
            showLoading()
            presenter.onLoadGames(query, true)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        initAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (query.isEmpty()) {
            inflater!!.inflate(R.menu.game_list_menu, menu)
            initMenu(menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> fragmentManager.popBackStack();
            R.id.game_list_source_setting -> presenter.onFavouritesClicked()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initToolbar() {
        listToolbar.apply {
            title = if (query.isEmpty()) {
                inflateMenu(R.menu.game_list_menu)
                resources.getString(R.string.app_name)
            } else {
                String.format(resources.getString(R.string.search_title), query)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(listToolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            if (query.isNotEmpty()) {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun initSwipeLayout() = swipeRefreshLayout.apply {
        setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent)
        setOnRefreshListener({ presenter.onLoadGames(query, isRefresh = true) })
    }

    private fun initAdapter() = recyclerView.apply {
        setHasFixedSize(true)
        val orientation = resources.configuration.orientation
        layoutManager = GridLayoutManager(context, if (orientation == ORIENTATION_PORTRAIT) 3 else 5)
        adapter = gameListAdapter
    }

    private fun initMenu(menu: Menu?) {
        val searchItem = menu!!.findItem(R.id.game_list_searchView)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setIconifiedByDefault(true)
            queryHint = resources.getString(R.string.search_game)
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    clearFocus()
                    presenter.onSearchClicked(queryText)
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean = false
            })
            setOnQueryTextFocusChangeListener({ _, hasFocus ->
                if (!hasFocus) isIconified = true
            })
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun addGameToList(games: List<GameViewModel>) {
        val adapter = recyclerView.adapter as GameListAdapter
        adapter.addGames(games)
    }

    override fun showNoDataFoundError() {
        Toast.makeText(context, resources.getString(R.string.error_no_data_found), Toast.LENGTH_SHORT).show()
    }

    override fun showInternetConnectionError() {
        Toast.makeText(context, resources.getString(R.string.error_internet_connection), Toast.LENGTH_SHORT).show()
    }

    override fun showDefaultError() {
        Toast.makeText(context, resources.getString(R.string.error_default), Toast.LENGTH_SHORT).show()
    }

    override fun clearList() {
        gameListAdapter.clearGames()
    }

    override fun navigateToGame(game: GameViewModel) {
        val detailsFragment = GameDetailFragment.newInstance(game, false)
        (activity as MainActivity).addFragment(detailsFragment)
    }

    override fun navigateToGameList(query: String) {
        val gameListFragment = GameListFragment.newInstance(query)
        (activity as MainActivity).addFragment(gameListFragment)
    }

    override fun navigateToFavourites() {
        val favouriteListFragment = FavouriteListFragment.newInstance()
        (activity as MainActivity).addFragment(favouriteListFragment)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
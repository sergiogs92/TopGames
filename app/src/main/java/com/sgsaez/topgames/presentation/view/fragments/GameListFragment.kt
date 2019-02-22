package com.sgsaez.topgames.presentation.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.presentation.view.adapters.GameListRenderer
import com.sgsaez.topgames.utils.condition
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.fragment_game_list.*

private const val QUERY_KEY: String = "QUERY_KEY"

fun newGameListInstance(query: String): GameListFragment = GameListFragment().apply {
    val args = Bundle()
    args.putString(QUERY_KEY, query)
    arguments = args
}

class GameListFragment : Fragment(), GameListView {

    private val presenter: GameListPresenter by lazy { component.presenter() }
    private lateinit var renderer: GameListRenderer
    private val component by lazy { topGamesApplication.component.plus(GameListFragmentModule()) }
    private var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        query = arguments?.getString(QUERY_KEY)!!
        initToolbar()
        initSwipeLayout()
        initRenderer()
        presenter.attachView(this)
        showLoading()
        presenter.onLoadGames(0, query)
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        initRenderer()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (query.isEmpty()) {
            inflater!!.inflate(R.menu.game_list_menu, menu)
            initMenu(menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
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
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (!hasFocus) isIconified = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> fragmentManager!!.popBackStack();
            R.id.game_list_source_setting -> presenter.onFavouritesClicked()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initToolbar() {
        listToolbar.apply {
            title = condition({ query.isEmpty() }, {
                inflateMenu(R.menu.game_list_menu)
                resources.getString(R.string.app_name)
            }, { String.format(resources.getString(R.string.search_title), query) })
        }
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(listToolbar)
        appCompatActivity.supportActionBar?.apply {
            if (query.isNotEmpty()) {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun initSwipeLayout() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent)
            if(query.isEmpty()) setOnRefreshListener { presenter.onLoadGames(0, query, isRefresh = true) }
        }

    }

    private fun initRenderer(){
        renderer = GameListRenderer(recyclerView, object: GameListRenderer.GameListener{
            override fun onLoadMore(requestPage: Int) = presenter.onLoadMore(requestPage)

            override fun onClickInGame(game: GameViewModel) = presenter.onGameClicked(game)

        })
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun addGameToList(isQuery: Boolean, games: List<GameViewModel>) {
        renderer.render(isQuery, games)
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
        renderer.clearGames()
    }

    override fun navigateToGame(game: GameViewModel) {
        val detailsFragment = newGameDetailInstance(game, false)
        (activity as MainActivity).addFragment(detailsFragment)
    }

    override fun navigateToGameList(query: String) {
        val gameListFragment = newGameListInstance(query)
        (activity as MainActivity).addFragment(gameListFragment)
    }

    override fun navigateToFavourites() {
        val favouriteListFragment = newFavouriteListInstance()
        (activity as MainActivity).addFragment(favouriteListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disposeComposite()
    }

}
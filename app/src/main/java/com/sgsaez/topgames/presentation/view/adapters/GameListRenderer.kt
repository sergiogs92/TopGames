package com.sgsaez.topgames.presentation.view.adapters

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.BaseViewHolder
import com.sgsaez.topgames.support.domains.Page
import com.sgsaez.topgames.support.loadUrl
import com.sgsaez.topgames.support.renderer.HolderSupport
import kotlinx.android.synthetic.main.game_item.*

private enum class GameState { DEFAULT, LOAD_MORE }

private const val MAX_REQUEST_PAGE = 3

class GameListRenderer(private val recyclerView: RecyclerView, private val listener: GameListener) {

    private var gameList: List<GameViewModel> = ArrayList()
    private var requestPage = -1
    private var shouldLoadMore: Boolean = true
    private var context: Context = recyclerView.context

    init {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val spanCount = getColumnsNumber()
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (GameState.values()[recyclerView.adapter!!.getItemViewType(position)]) {
                    GameState.DEFAULT -> 1
                    GameState.LOAD_MORE -> getColumnsNumber()
                }
            }
        }
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = GameListRecyclerViewAdapter()
        }
    }

    fun render(page: Page<GameViewModel>) {
        gameList = page.items
        requestPage = page.requestedPage
        if (requestPage == MAX_REQUEST_PAGE || page.query.isNotEmpty()) shouldLoadMore = false
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    fun clearGames() {
        gameList = ArrayList()
        shouldLoadMore = true
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun getColumnsNumber(): Int {
        val orientation = context.resources.configuration.orientation
        val portraitColumns = context.resources.getInteger(R.integer.portrait_columns)
        val landscapeColumns = context.resources.getInteger(R.integer.landscape_columns)
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) portraitColumns else landscapeColumns
    }

    private inner class GameListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is GameViewHolder -> holder.render(gameList[position], listener)
                is LoadMoreHolder -> listener.onLoadMore(requestPage + 1)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (GameState.values()[viewType]) {
                GameState.DEFAULT -> GameViewHolder(parent)
                GameState.LOAD_MORE -> LoadMoreHolder(parent)
            }
        }

        override fun getItemCount(): Int = gameList.size + if (shouldLoadMore && gameList.isNotEmpty()) 1 else 0

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                gameList.size -> GameState.LOAD_MORE.ordinal
                else -> GameState.DEFAULT.ordinal
            }
        }

    }

    private inner class GameViewHolder internal constructor(parent: ViewGroup) : BaseViewHolder(HolderSupport.getView(parent, R.layout.game_item)) {

        fun render(game: GameViewModel, listener: GameListener) {
            name.text = game.name
            image.loadUrl(game.imageUrl)
            image.setOnClickListener { listener.onClickInGame(game) }
        }

    }

    private inner class LoadMoreHolder(parent: ViewGroup) : BaseViewHolder(HolderSupport.getView(parent, R.layout.load_more_item))


    interface GameListener {

        fun onClickInGame(game: GameViewModel)

        fun onLoadMore(requestPage: Int)
    }

}
package com.sgsaez.topgames.presentation.view.renderers

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.BaseViewHolder
import com.sgsaez.topgames.support.loadUrl
import com.sgsaez.topgames.support.renderer.HolderSupport
import kotlinx.android.synthetic.main.game_item.view.*

class FavouriteListRenderer(private val recyclerView: RecyclerView, private val listener: FavouriteListener) {

    private var favouriteList: List<GameViewModel> = ArrayList()
    private var context: Context = recyclerView.context

    init {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val spanCount = getColumnsNumber()
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = FavouriteListRecyclerViewAdapter()
        }
    }

    fun render(favourites: List<GameViewModel>) {
        favouriteList = favourites
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    fun removeFavourite(favouriteGame: GameViewModel) {
        favouriteList = favouriteList.filter { it != favouriteGame }
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun getColumnsNumber(): Int {
        val orientation = context.resources.configuration.orientation
        val portraitColumns = context.resources.getInteger(R.integer.portrait_columns)
        val landscapeColumns = context.resources.getInteger(R.integer.landscape_columns)
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) portraitColumns else landscapeColumns
    }

    private inner class FavouriteListRecyclerViewAdapter : RecyclerView.Adapter<FavouriteViewHolder>() {

        override fun getItemCount() = favouriteList.size

        override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) = holder.bind(favouriteList[position], listener)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavouriteViewHolder(parent)

    }

    private inner class FavouriteViewHolder(parent: ViewGroup)  : BaseViewHolder(HolderSupport.getView(parent, R.layout.game_item)) {
        fun bind(favourite: GameViewModel, listener: FavouriteListener) = with(itemView) {
            name.text = favourite.name
            image.loadUrl(favourite.imageUrl)
            close.setOnClickListener {
                close.visibility = View.GONE
                listener.onClickInClose(favourite)
            }
            setOnClickListener { listener.onClickInGame(favourite) }
            setOnLongClickListener {
                close.visibility = if (close.visibility == View.GONE) View.VISIBLE else View.GONE
                true
            }
        }
    }

    interface FavouriteListener {

        fun onClickInGame(favouriteGame: GameViewModel)

        fun onClickInClose(favouriteGame: GameViewModel)

    }

}
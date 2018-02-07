package com.sgsaez.topgames.presentation.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.utils.inflate
import com.sgsaez.topgames.utils.loadUrl
import kotlinx.android.synthetic.main.game_item.view.*

class FavouriteListAdapter(private val favouriteList: MutableList<GameViewModel>,
                           private val listener: FavouriteListener) : RecyclerView.Adapter<FavouriteListAdapter.FavouriteViewHolder>() {

    override fun getItemCount() = favouriteList.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favouriteList[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavouriteViewHolder(parent.inflate(R.layout.game_item))

    fun addFavourites(favourites: List<GameViewModel>) {
        favouriteList.addAll(favourites)
        notifyDataSetChanged()
    }

    fun removeFavourite(favouriteGame: GameViewModel) {
        favouriteList.remove(favouriteGame)
        notifyDataSetChanged()
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favourite: GameViewModel, listener: FavouriteListener) = with(itemView) {
            name.text = favourite.name
            image.loadUrl(favourite.imageUrl)
            close.setOnClickListener {
                close.visibility = View.GONE
                listener.onClickInClose(favourite)
            }
            setOnClickListener { listener.onClickInGame(favourite) }
            setOnLongClickListener {
                if (close.visibility == View.GONE) close.visibility = View.VISIBLE else close.visibility = View.GONE
                true
            }
        }
    }

    interface FavouriteListener {

        fun onClickInGame(favouriteGame: GameViewModel)

        fun onClickInClose(favouriteGame: GameViewModel)
    }

}
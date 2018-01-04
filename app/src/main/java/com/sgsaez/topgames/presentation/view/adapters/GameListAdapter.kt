package com.sgsaez.topgames.presentation.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.utils.inflate
import com.sgsaez.topgames.utils.loadUrl
import kotlinx.android.synthetic.main.game_item.view.*

class GameListAdapter(
        private val gameList: MutableList<Game>,
        private val listener: (Game, View) -> Unit) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    override fun getItemCount() = gameList.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) = holder.bind(gameList[position], listener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GameViewHolder(parent.inflate(R.layout.game_item))

    fun addGames(games: List<Game>) {
        gameList.addAll(games)
        notifyDataSetChanged()
    }

    fun clearGames() {
        gameList.clear()
    }

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game, listener: (Game, View) -> Unit) = with(itemView) {
            name.text = game.name
            image.loadUrl(game.image.url)
            setOnClickListener { listener(game, image) }
        }
    }

}
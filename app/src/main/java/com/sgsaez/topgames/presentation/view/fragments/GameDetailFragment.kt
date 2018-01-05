package com.sgsaez.topgames.presentation.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgsaez.topgames.R
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.NotParcelled.fromNotParcelled
import com.sgsaez.topgames.utils.isLollipopOrAbove
import com.sgsaez.topgames.utils.loadUrl
import com.sgsaez.topgames.utils.topGamesApplication
import kotlinx.android.synthetic.main.description_item.*
import kotlinx.android.synthetic.main.fragment_game_detail.*

class GameDetailFragment : Fragment(), GameDetailView {

    private val presenter: GameDetailPresenter by lazy { component.presenter() }
    private val component by lazy { topGamesApplication.component.plus(GameDetailFragmentModule()) }

    companion object {
        private val GAME_KEY: String = "GAME_KEY"
        fun newInstance(game: String): GameDetailFragment {
            val fragment = GameDetailFragment()
            val args = Bundle()
            args.putString(GAME_KEY, game)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_game_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        val game = fromNotParcelled(arguments.getString(GAME_KEY), Game::class.java)
        initFloatingButton(game)
        presenter.attachView(this)
        presenter.paintDetail(game)
    }

    private fun initToolbar() {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { activity.onBackPressed() }
        }
    }

    private fun initFloatingButton(game: Game) {
        isLollipopOrAbove() {
            floatingButton.apply {
                visibility = View.VISIBLE
                setOnClickListener { presenter.setUpIntentShared(game, image) }
            }
        }
    }

    override fun addTitleToolbar(name: String) {
        toolbar.title = name
    }

    override fun addDescription(content: String) {
        description.text = content
    }

    override fun addImage(url: String) {
        image.loadUrl(url)
    }
}


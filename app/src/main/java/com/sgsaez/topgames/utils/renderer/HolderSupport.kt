package com.sgsaez.topgames.utils.renderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

object HolderSupport {

    fun getView(parent: ViewGroup, layout: Int): View {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(layout, parent, false)
    }

    fun getView(context: Context, layout: Int): View {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(layout, null)
    }

}

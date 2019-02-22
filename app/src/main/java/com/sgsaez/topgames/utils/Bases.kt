package com.sgsaez.topgames.utils

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder(override val containerView: View?) : LayoutContainer, RecyclerView.ViewHolder(containerView!!)

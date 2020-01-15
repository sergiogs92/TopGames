package com.sgsaez.topgames.support

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder(override val containerView: View?) : LayoutContainer, RecyclerView.ViewHolder(containerView!!)

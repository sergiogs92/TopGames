package com.sgsaez.topgames.di.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class Factory<VM>(private val viewModel: VM) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
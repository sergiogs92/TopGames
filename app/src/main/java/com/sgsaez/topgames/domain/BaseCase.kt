package com.sgsaez.topgames.domain

import com.sgsaez.topgames.support.SchedulerProvider

abstract class BaseCase(private val schedulerProvider: SchedulerProvider) {

    fun uiExecute(uiFun: () -> Unit) = schedulerProvider.uiExecute { uiFun() }

    fun asyncExecute(asyncFun: () -> Unit) = schedulerProvider.asyncExecute { asyncFun() }

}
package com.sgsaez.topgames.support

interface SchedulerProvider {
    fun uiExecute(uiFun: () -> Unit)
    fun asyncExecute(asyncFun: () -> Unit)
}
package com.sgsaez.topgames.support

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AppSchedulerProvider : SchedulerProvider {
    override fun uiExecute(uiFun: () -> Unit) {
        doAsync {
            uiThread {
                uiFun()
            }
        }
    }

    override fun asyncExecute(asyncFun: () -> Unit) {
        doAsync {
            asyncFun()
        }
    }

}
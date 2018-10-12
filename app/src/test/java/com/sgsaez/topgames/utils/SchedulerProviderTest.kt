package com.sgsaez.topgames.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class SchedulerProviderTest(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun uiScheduler(): Scheduler = testScheduler

    override fun ioScheduler(): Scheduler = testScheduler

}
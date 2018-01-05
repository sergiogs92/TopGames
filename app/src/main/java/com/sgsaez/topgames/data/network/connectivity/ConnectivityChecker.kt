package com.sgsaez.topgames.data.network.connectivity

interface ConnectivityChecker {
    fun isOnline(): Boolean
}
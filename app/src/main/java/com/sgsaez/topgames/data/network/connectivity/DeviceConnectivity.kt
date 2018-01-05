package com.sgsaez.topgames.data.network.connectivity

import android.content.Context
import android.net.ConnectivityManager

class DeviceConnectivity(private val context: Context) : ConnectivityChecker {

    override fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
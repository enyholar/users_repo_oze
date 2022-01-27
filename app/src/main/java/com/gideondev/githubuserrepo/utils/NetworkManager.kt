package com.gideondev.githubuserrepo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

class NetworkManager @Inject constructor(val context: Context) {

    fun isNetworkAvailable(): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo ?: return false
        return activeNetwork.isConnected
    }
}
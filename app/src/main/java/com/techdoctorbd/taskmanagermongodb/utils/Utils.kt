package com.techdoctorbd.taskmanagermongodb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper


fun checkConnection(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connMgr.activeNetworkInfo
    return activeNetworkInfo != null
}

fun runDelayed(delayMillis: Long = 200, action: () -> Unit) =
    Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)

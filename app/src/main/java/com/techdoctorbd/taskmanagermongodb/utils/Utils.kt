package com.techdoctorbd.taskmanagermongodb.utils

import android.content.Context
import android.net.ConnectivityManager


fun checkConnection(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connMgr.activeNetworkInfo
    return activeNetworkInfo != null
}

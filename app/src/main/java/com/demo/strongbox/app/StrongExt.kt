package com.demo.strongbox.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.demo.strongbox.R
import java.lang.Exception

fun View.showView(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
}

fun serverIcon(string: String)=when(string){
    else-> R.drawable.fast
}

fun serverIcon2(string: String)=when(string){
    else-> R.drawable.fast2
}

fun Context.getNet0914Status(): Int {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return 2
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return 0
        }
    } else {
        return 1
    }
    return 1
}

fun Context.toast(s: String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

package com.demo.strongbox.app

import android.app.ActivityManager
import android.app.Application
import com.demo.strongbox.activity0914.Home0914Activity
import com.demo.strongbox.fire.Read0914Firebase
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV

lateinit var mStrong: Strong
class Strong:Application() {
    override fun onCreate() {
        super.onCreate()
        mStrong=this
        Core.init(this,Home0914Activity::class)
        MMKV.initialize(this)
        if (!packageName.equals(na(this))){
            return
        }
        Read0914Firebase.read()
        ActivityCallBack0914.register(this)
    }

    private fun na(applicationContext: Application): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid === pid) {
                processName = process.processName
            }
        }
        return processName
    }
}
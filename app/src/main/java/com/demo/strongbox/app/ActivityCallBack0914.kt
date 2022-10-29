package com.demo.strongbox.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.strongbox.activity0914.Home0914Activity
import com.demo.strongbox.activity0914.Main0914Activity
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ActivityCallBack0914 {
    var activity0914Front=true
    var refresh0914HomeAd=true
    private var to0914MainActivity=false
    private var launch0914Job: Job?=null


    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(callback)
    }

    private val callback=object : Application.ActivityLifecycleCallbacks{
        private var acNum=0
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            acNum++
            stopJob()
            if (acNum==1){
                activity0914Front=true
                if (to0914MainActivity){
                    if (ActivityUtils.isActivityExistsInStack(Home0914Activity::class.java)){
                        activity.startActivity(Intent(activity, Main0914Activity::class.java))
                    }
                }
                to0914MainActivity=false
            }
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            acNum--
            if (acNum<=0){
                activity0914Front=false
                refresh0914HomeAd=true
                launch0914Job= GlobalScope.launch {
                    delay(3000L)
                    to0914MainActivity=true
                    ActivityUtils.finishActivity(Main0914Activity::class.java)
                    ActivityUtils.finishActivity(AdActivity::class.java)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}

        private fun stopJob(){
            launch0914Job?.cancel()
            launch0914Job=null
        }
    }
}
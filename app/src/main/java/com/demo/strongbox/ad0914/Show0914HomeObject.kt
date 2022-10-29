package com.demo.strongbox.ad0914

import com.demo.strongbox.activity0914.Base0914Activity
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.*

class Show0914HomeObject(
    private val base0914Activity: Base0914Activity,
    private val t:String,
) :BaseShow0914HomeAd(t,base0914Activity){

    private var local:NativeAd?=null
    private var launch0914Job:Job?=null

    fun loop0914Call(){
        Load0914AdObject.loadLogic(t)
        stopLaunch0914Job()
        launch0914Job = GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (base0914Activity.resume0914){
                while (true) {
                    if (!isActive) {
                        break
                    }
                    val adData = Load0914AdObject.getAdData(t)
                    if (null != adData && adData is NativeAd&&base0914Activity.resume0914 ) {
                        cancel()
                        if (local != null) {
                            local!!.destroy()
                        }
                        local = adData
                        show(adData)
                    }
                    delay(1000L)
                }
            }
        }
    }

    fun stopLaunch0914Job(){
        launch0914Job?.cancel()
        launch0914Job=null
    }
}
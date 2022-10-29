package com.demo.strongbox.ad0914

import com.demo.strongbox.activity0914.Base0914Activity
import com.demo.strongbox.app.printStrong
import com.demo.strongbox.fire.Read0914Firebase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Show0914MainObject(
    private val base0914Activity: Base0914Activity,
    private val adLocation:String,
    private val next:()->Unit
) {


    fun show0914MainAd(finish:(jumpAc:Boolean)->Unit){
        val adData = Load0914AdObject.getAdData(adLocation)
        if (null==adData&&Read0914Firebase.is0914Limit()){
            finish.invoke(true)
            return
        }

        if (null!=adData){
            if (Load0914AdObject.mainAdShowing||!base0914Activity.resume0914){
                printStrong("not show $adLocation ad ")
                finish.invoke(false)
                return
            }
            finish.invoke(false)
            if (adData is AppOpenAd){
                showKp(adData)
            }
            if (adData is InterstitialAd){
                showCp(adData)
            }
        }
    }

    private fun showKp(ad: AppOpenAd){
        ad.fullScreenContentCallback=listener
        ad.show(base0914Activity)
    }

    private fun showCp(ad: InterstitialAd){
        ad.fullScreenContentCallback=listener
        ad.show(base0914Activity)
    }

    private val listener=object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            Load0914AdObject.mainAdShowing=false
            onNext()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            Read0914Firebase.addShow0914Num()
            Load0914AdObject.mainAdShowing=true
            Load0914AdObject.removeAd(adLocation)
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            Load0914AdObject.mainAdShowing=false
            Load0914AdObject.removeAd(adLocation)
            onNext()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            Read0914Firebase.addClick0914Num()
        }
    }

    private fun onNext(){
        if (adLocation==Ad0914LocationStr.AD_CONNECT){
            Load0914AdObject.loadLogic(adLocation)
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(200L)
            if (base0914Activity.resume0914){
                next.invoke()
            }
        }
    }
}
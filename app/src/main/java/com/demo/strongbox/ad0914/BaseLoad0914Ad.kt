package com.demo.strongbox.ad0914

import com.demo.strongbox.app.mStrong
import com.demo.strongbox.app.printStrong
import com.demo.strongbox.fire.Read0914Firebase
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions

abstract class BaseLoad0914Ad {
    var mainAdShowing=false
    val loadingAdTag= arrayListOf<String>()
    val adData= hashMapOf<String,Ad0914Data>()

    fun loadLogic(t:String,again:Boolean=true){
        if (isLoadingAd(t)){
            printStrong("$t loading")
            return
        }
        if (hadAdData(t)){
            printStrong("$t had cache")
            return
        }

        if (Read0914Firebase.is0914Limit()){
            printStrong("max limit")
            return
        }

        val confAdList = getConfAdList(t)
        if (confAdList.isNullOrEmpty()){
            printStrong("$t ad list null")
            return
        }
        loadingAdTag.add(t)
        loopLoadAd(t,confAdList.iterator(),again)
    }

    protected abstract fun isLoadingAd(t: String):Boolean

    protected abstract fun hadAdData(t: String):Boolean

    protected abstract fun getConfAdList(t: String):List<ConfAd0914En>

    protected abstract fun loopLoadAd(t:String,iterator: Iterator<ConfAd0914En>,again:Boolean)

    protected fun loadAdByType(t:String,confAd0914En: ConfAd0914En,loadResult:(ad0914Data:Ad0914Data)->Unit){
        printStrong("load $t ad,${confAd0914En.toString()}")
        if (confAd0914En.type_strong0914=="kp"){
            AppOpenAd.load(
                mStrong,
                confAd0914En.id_strong0914,
                AdRequest.Builder().build(),
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback(){
                    override fun onAdLoaded(p0: AppOpenAd) {
                        loadResult.invoke(
                            Ad0914Data(
                                loadTime = System.currentTimeMillis(),
                                adData=p0,
                                )
                        )
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        loadResult.invoke(
                            Ad0914Data(
                                loadFailMsg = p0.message
                            )
                        )
                    }
                }
            )
        }
        if (confAd0914En.type_strong0914=="cp"){
            InterstitialAd.load(
                mStrong,
                confAd0914En.id_strong0914,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        loadResult.invoke(
                            Ad0914Data(
                                loadFailMsg = p0.message
                            )
                        )
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        loadResult.invoke(
                            Ad0914Data(
                                loadTime = System.currentTimeMillis(),
                                adData=p0,
                            )
                        )
                    }
                }
            )
        }
        if (confAd0914En.type_strong0914=="ys"){
            AdLoader.Builder(
                mStrong,
                confAd0914En.id_strong0914
            ).forNativeAd {
                loadResult.invoke(
                    Ad0914Data(
                        loadTime = System.currentTimeMillis(),
                        adData=it,
                    )
                )
            }
                .withAdListener(object : AdListener(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        loadResult.invoke(
                            Ad0914Data(
                                loadFailMsg = p0.message
                            )
                        )
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Read0914Firebase.addClick0914Num()
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .setAdChoicesPlacement(
                            NativeAdOptions.ADCHOICES_TOP_LEFT
                        )
                        .build()
                )
                .build()
                .loadAd(AdRequest.Builder().build())
        }
    }
}
package com.demo.strongbox.ad0914

import com.demo.strongbox.app.printStrong
import com.demo.strongbox.fire.Read0914Firebase
import org.json.JSONObject

object Load0914AdObject :BaseLoad0914Ad(){

    override fun isLoadingAd(t: String): Boolean = loadingAdTag.contains(t)

    override fun hadAdData(t: String): Boolean {
        if (adData.containsKey(t)){
            val ad0914Data = adData[t]
            if (null!=ad0914Data?.adData){
                if (ad0914Data.guoqi()){
                    removeAd(t)
                }else{
                    return true
                }
            }
        }
        return false
    }

    override fun getConfAdList(t: String): List<ConfAd0914En> {
        val list= arrayListOf<ConfAd0914En>()
        try {
            val jsonArray = JSONObject(Read0914Firebase.readAdStr0914()).getJSONArray(t)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    ConfAd0914En(
                        jsonObject.optString("strong_0914_id"),
                        jsonObject.optInt("strong_0914_sort"),
                        jsonObject.optString("strong_0914_type"),
                        jsonObject.optString("strong_0914_source"),
                    )
                )
            }
        }catch (e:Exception){}
        return list.filter { it.source_strong0914 == "admob" }.sortedByDescending { it.sort_strong0914 }
    }

    override fun loopLoadAd(t: String, iterator: Iterator<ConfAd0914En>, again: Boolean) {
        val next = iterator.next()
        loadAdByType(t,next){
            if (it.loadFailMsg.isNullOrEmpty()){
                printStrong("$t ad load success")
                loadingAdTag.remove(t)
                if (null!=it.adData){
                    adData[t]=it
                }
            }else{
                printStrong("$t ad load fail,${it.loadFailMsg}")
                if (iterator.hasNext()){
                    loopLoadAd(t,iterator,again)
                }else{
                    loadingAdTag.remove(t)
                    if (t==Ad0914LocationStr.AD_MAIN&&again){
                        loadLogic(t,again = false)
                    }
                }
            }
        }
    }

    fun removeAd(t: String){
        adData.remove(t)
    }

    fun getAdData(t: String)=adData[t]?.adData
}
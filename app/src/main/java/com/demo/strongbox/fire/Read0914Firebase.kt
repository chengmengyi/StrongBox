package com.demo.strongbox.fire

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object Read0914Firebase {
    val ci0914= arrayListOf<String>()
    val se0914= arrayListOf<Server0914En>()

    private var max0914ClickNum=15
    private var max0914ShowNum=50

    private var current0914ShowNum=0
    private var current0914ClickNum=0

    fun read(){
        saveRaoStr(Local0914Conf.LOCAL_RAO_0914)
        createServerId(Local0914Conf.server0914List)

        getLocalCurrentNum()

//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                saveRaoStr(remoteConfig.getString("strong_rao_0914"))
//                saveCi0914(remoteConfig.getString("strong_ci_0914"))
//                saveSe0914(remoteConfig.getString("strong_se_0914"))
//                saveAd0914(remoteConfig.getString("strong_ad_0914"))
//            }
//        }
    }


    fun is0914Limit():Boolean{
        return current0914ShowNum>= max0914ShowNum|| current0914ClickNum>= max0914ClickNum
    }

    private fun getLocalCurrentNum(){
        current0914ShowNum=MMKV.defaultMMKV().decodeInt(getSaveNumKey("strong_0914_show"),0)
        current0914ClickNum=MMKV.defaultMMKV().decodeInt(getSaveNumKey("strong_0914_click"),0)
    }

    fun addClick0914Num(){
        current0914ClickNum++
        MMKV.defaultMMKV().encode(getSaveNumKey("strong_0914_click"),current0914ClickNum)
    }

    fun addShow0914Num(){
        current0914ShowNum++
        MMKV.defaultMMKV().encode(getSaveNumKey("strong_0914_show"),current0914ShowNum)
    }

    private fun saveAd0914(string: String){
        MMKV.defaultMMKV().encode("strong_ad_0914",string)
        try {
            val jsonObject = JSONObject(string)
            max0914ShowNum=jsonObject.optInt("strong_0914_show")
            max0914ClickNum=jsonObject.optInt("strong_0914_click")
        }catch (e:Exception){

        }
    }

    private fun getSaveNumKey(type:String)="$type...${SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))}"


    fun readAdStr0914():String{
        val strong_ad_0914 = MMKV.defaultMMKV().decodeString("strong_ad_0914", "")
        if(strong_ad_0914.isNullOrEmpty()) return Local0914Conf.STRONG_AD_0914 else return strong_ad_0914
    }

    private fun saveSe0914(string: String){
        se0914.clear()
        try {
            val jsonArray = JSONObject(string).getJSONArray("strong_se_0914")
            for (index in 0 until jsonArray.length()){
                val json0914Object = jsonArray.getJSONObject(index)
                se0914.add(
                    Server0914En(
                        json0914Object.optString("host_strong_0914"),
                        json0914Object.optInt("port_strong_0914"),
                        json0914Object.optString("pwd_strong_0914"),
                        json0914Object.optString("country_strong_0914"),
                        json0914Object.optString("city_strong_0914"),
                        json0914Object.optString("method_strong_0914"),
                    )
                )
            }
            createServerId(se0914)
        }catch (e:Exception){

        }
    }

    private fun saveCi0914(string: String){
        try {
            ci0914.clear()
            val jsonArray = JSONObject(string).getJSONArray("strong_ci_0914")
            for (index in 0 until jsonArray.length()){
                ci0914.add(jsonArray.optString(index))
            }
        }catch (e:Exception){

        }
    }

    private fun saveRaoStr(string: String){
        MMKV.mmkvWithID("strong").encode("strong_json",string)
    }

    private fun createServerId(list: List<Server0914En>){
        GlobalScope.launch {
            list.forEach {
                it.createServerId()
            }
        }
    }
}
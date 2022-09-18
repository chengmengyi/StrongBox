package com.demo.strongbox.fire

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Read0914Firebase {
    fun read(){
        saveRaoStr(Local0914Conf.LOCAL_RAO_0914)
        createServerId(Local0914Conf.server0914List)


        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful){
                saveRaoStr(remoteConfig.getString("strong_rao_0914"))
            }
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
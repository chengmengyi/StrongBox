package com.demo.strongbox.fire

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Read0914Firebase {

    fun read(){
        saveRaoStr(Local0914Conf.LOCAL_RAO_0914)
        createServerId(Local0914Conf.server0914List)

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
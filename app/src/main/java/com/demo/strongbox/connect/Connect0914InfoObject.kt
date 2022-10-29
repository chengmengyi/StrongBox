package com.demo.strongbox.connect

import com.demo.strongbox.fire.Local0914Conf
import com.demo.strongbox.fire.Read0914Firebase
import com.demo.strongbox.fire.Server0914En

object Connect0914InfoObject {

    fun getServerList()=Read0914Firebase.se0914.ifEmpty { Local0914Conf.server0914List }

    fun newFastSever0914En()=Server0914En(country_0914_strong = "Super Fast")

    fun getFast0914ServerEn():Server0914En{
        val serverList = getServerList()
        if (!Read0914Firebase.ci0914.isNullOrEmpty()){
            val filter = serverList.filter { Read0914Firebase.ci0914.contains(it.city_0914_strong) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }
}
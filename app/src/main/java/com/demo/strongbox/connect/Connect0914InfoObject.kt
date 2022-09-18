package com.demo.strongbox.connect

import com.demo.strongbox.fire.Local0914Conf
import com.demo.strongbox.fire.Server0914En

object Connect0914InfoObject {

    fun getServerList()=Local0914Conf.server0914List

    fun newFastSever0914En()=Server0914En(country_0914_strong = "Super Fast")

    fun getFast0914ServerEn()= getServerList().random()
}
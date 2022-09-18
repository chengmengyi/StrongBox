package com.demo.strongbox.fire

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class Server0914En(
    val host_0914_strong:String="",
    val port_0914_strong:Int=0,
    val pwd_0914_strong:String="",
    val country_0914_strong:String="",
    val city_0914_strong:String="",
    val method_0914_strong:String=""
) {

    fun createServerId(){
        val profile = Profile(
            id = 0L,
            name = "$country_0914_strong - $city_0914_strong",
            host = host_0914_strong,
            remotePort = port_0914_strong,
            password = pwd_0914_strong,
            method = method_0914_strong
        )

        var id:Long?=null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort==profile.remotePort&&it.host==profile.host){
                id=it.id
                return@forEach
            }
        }
        if (null==id){
            ProfileManager.createProfile(profile)
        }else{
            profile.id=id!!
            ProfileManager.updateProfile(profile)
        }
    }

    fun getServer0916ID():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==host_0914_strong&&it.remotePort==port_0914_strong){
                return it.id
            }
        }
        return 0L
    }

    fun isFast()=country_0914_strong=="Super Fast"&&host_0914_strong.isEmpty()
}
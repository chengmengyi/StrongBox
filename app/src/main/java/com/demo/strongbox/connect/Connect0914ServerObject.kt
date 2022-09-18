package com.demo.strongbox.connect

import com.demo.strongbox.activity0914.Home0914Activity
import com.demo.strongbox.inter.IConnect0914State
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Connect0914ServerObject :ShadowsocksConnection.Callback{
    private var state = BaseService.State.Idle
    private var home0914Activity:Home0914Activity?=null
    private val sc= ShadowsocksConnection(true)

    var server0914En=Connect0914InfoObject.newFastSever0914En()
    var lastServer0914En=Connect0914InfoObject.newFastSever0914En()

    private var iConnect0914State:IConnect0914State?=null

    fun initConnection(home0914Activity: Home0914Activity,iConnect0914State:IConnect0914State){
        this.home0914Activity=home0914Activity
        this.iConnect0914State=iConnect0914State
        sc.connect(home0914Activity,this)
    }

    fun connect0914Server(){
        state= BaseService.State.Connecting
        GlobalScope.launch {
            if (server0914En.isFast()){
                DataStore.profileId = Connect0914InfoObject.getFast0914ServerEn().getServer0916ID()
            }else{
                DataStore.profileId = server0914En.getServer0916ID()
            }
            Core.startService()
        }
        Connect0914TimeObject.t=0L
    }

    fun disconnect0914Server(){
        state= BaseService.State.Stopping
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun connected0914()= state==BaseService.State.Connected

    fun disconnected0914()= state==BaseService.State.Stopped

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        this.state=state
        if (disconnected0914()){
            Connect0914TimeObject.stopTime()
            iConnect0914State?.disconnectedCallback()
        }

        if (connected0914()){
            lastServer0914En= server0914En
            Connect0914TimeObject.startTime()
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        this.state=state
        if (connected0914()){
            lastServer0914En= server0914En
            Connect0914TimeObject.startTime()
            iConnect0914State?.connectedCallback()
        }
    }

    override fun onBinderDied() {
        if (null!= home0914Activity){
            sc.disconnect(home0914Activity!!)
        }
    }

    fun onDestroy(){
        onBinderDied()
        iConnect0914State=null
    }
}
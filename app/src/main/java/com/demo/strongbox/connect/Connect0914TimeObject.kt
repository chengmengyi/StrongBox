package com.demo.strongbox.connect

import com.demo.strongbox.inter.IConnect0914Time
import kotlinx.coroutines.*
import java.lang.Exception

object Connect0914TimeObject {
    var t=0L
    private var timeJob:Job?=null
    private val callbacks= arrayListOf<IConnect0914Time>()

    fun addCallback(iConnect0914State: IConnect0914Time){
        callbacks.add(iConnect0914State)
    }

    fun deleteCallback(iConnect0914State: IConnect0914Time){
        callbacks.remove(iConnect0914State)
    }

    fun startTime(){
        if (null!= timeJob) return
        timeJob = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                callbacks.forEach {
                    it.connect0914Time(trans())
                }
                t++
                delay(1000L)
            }
        }
    }

    fun stopTime(){
        timeJob?.cancel()
        timeJob=null
    }

    fun getCurrentTime()= trans()

    private fun trans():String{
        try {
            val shi=t/3600
            val fen= (t % 3600) / 60
            val miao= (t % 3600) % 60
            val s=if (shi<10) "0${shi}" else shi
            val f=if (fen<10) "0${fen}" else fen
            val m=if (miao<10) "0${miao}" else miao
            return "${s}:${f}:${m}"
        }catch (e: Exception){}
        return "00:00:00"
    }
}
package com.demo.strongbox.ad0914

class Ad0914Data(
    val loadTime:Long=0,
    val adData:Any?=null,
    val loadFailMsg:String=""
) {
    fun guoqi()=(System.currentTimeMillis() - loadTime) >=1000L*3600L
}
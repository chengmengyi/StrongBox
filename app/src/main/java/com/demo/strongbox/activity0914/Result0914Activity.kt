package com.demo.strongbox.activity0914

import android.os.Bundle
import com.demo.strongbox.R
import com.demo.strongbox.ad0914.Ad0914LocationStr
import com.demo.strongbox.ad0914.Show0914HomeObject
import com.demo.strongbox.app.serverIcon
import com.demo.strongbox.connect.Connect0914ServerObject
import com.demo.strongbox.connect.Connect0914TimeObject
import com.demo.strongbox.inter.IConnect0914Time
import kotlinx.android.synthetic.main.activity_result_0914.*

class Result0914Activity:Base0914Activity(R.layout.activity_result_0914), IConnect0914Time {
    private var state=false
    private val show0914Home by lazy { Show0914HomeObject(this, Ad0914LocationStr.AD_RESULT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_back.setOnClickListener { finish() }

        state = intent.getBooleanExtra("state", false)
        tv_result_connect_time.isSelected=state
        tv_title.text=if (state) "Connect success" else "Disconnected"

        iv_result_server_icon.setImageResource(serverIcon(Connect0914ServerObject.lastServer0914En.country_0914_strong))

        if (state){
            Connect0914TimeObject.addCallback(this)
        }else{
            connect0914Time(Connect0914TimeObject.getCurrentTime())
        }
    }

    override fun connect0914Time(time: String) {
        tv_result_connect_time.text=time
    }

    override fun onResume() {
        super.onResume()
        show0914Home.loop0914Call()
    }

    override fun onDestroy() {
        super.onDestroy()
        show0914Home.stopLaunch0914Job()
        Connect0914TimeObject.deleteCallback(this)
    }
}
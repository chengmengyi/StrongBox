package com.demo.strongbox.activity0914

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.VpnService
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.strongbox.R
import com.demo.strongbox.ad0914.Ad0914LocationStr
import com.demo.strongbox.ad0914.Load0914AdObject
import com.demo.strongbox.ad0914.Show0914HomeObject
import com.demo.strongbox.ad0914.Show0914MainObject
import com.demo.strongbox.adapter0914.ServerListAdapter
import com.demo.strongbox.app.*
import com.demo.strongbox.connect.Connect0914ServerObject
import com.demo.strongbox.connect.Connect0914TimeObject
import com.demo.strongbox.fire.Local0914Conf
import com.demo.strongbox.fire.Server0914En
import com.demo.strongbox.inter.IConnect0914State
import com.demo.strongbox.inter.IConnect0914Time
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.activity_home_0914.*
import kotlinx.android.synthetic.main.layout_connect.*
import kotlinx.android.synthetic.main.layout_home_bottom.*
import kotlinx.android.synthetic.main.layout_server_list.*
import kotlinx.android.synthetic.main.layout_set.*
import kotlinx.coroutines.*
import java.lang.Exception

class Home0914Activity :Base0914Activity(R.layout.activity_home_0914), IConnect0914State, IConnect0914Time {
    private var preType=0
    private var home0914CanClick=true
    private var permission=false
    private var connectServer=true
    private var launch0914Job:Job?=null
    private val register0914=registerForActivityResult(StartService()) {
        if (!it && permission) {
            permission = false
            startConnectServer()
        } else {
            home0914CanClick=true
            toast("Connected fail")
        }
    }

    private val show0914Home by lazy { Show0914HomeObject(this,Ad0914LocationStr.AD_HOME) }
    private val show0914connect by lazy { Show0914MainObject(this,Ad0914LocationStr.AD_CONNECT){toResult0914Activity()} }
    private val show0914back by lazy { Show0914MainObject(this,Ad0914LocationStr.AD_BACK){updateHomeUI(preType)} }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Connect0914TimeObject.addCallback(this)
        Connect0914ServerObject.initConnection(this,this)
        updateHomeUI(1)
        setAdapter()
        setListener()
    }

    private fun setAdapter(){
        review.apply {
            layoutManager=GridLayoutManager(this@Home0914Activity,2)
            adapter=ServerListAdapter(this@Home0914Activity){
                clickServer(it)
            }
        }
    }

    private fun clickServer(server0914En: Server0914En){
        val current = Connect0914ServerObject.server0914En
        if (current.host_0914_strong!=server0914En.host_0914_strong&&Connect0914ServerObject.connected0914()){
            AlertDialog.Builder(this).apply {
                setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
                setPositiveButton("sure") { _, _ ->
                    Connect0914ServerObject.server0914En=server0914En
                    updateHomeUI(1,showAd = false)
                    connectDisconnectServer()
                }
                setNegativeButton("cancel",null)
                show()
            }
            return
        }
        Connect0914ServerObject.server0914En=server0914En
        updateHomeUI(1,showAd = false)
        if (current.host_0914_strong==server0914En.host_0914_strong&&Connect0914ServerObject.connected0914()){

        }else{
            connectDisconnectServer()
        }

    }

    private fun setListener(){
        llc_server.setOnClickListener { updateHomeUI(0) }
        iv_bottom_home_btn.setOnClickListener { updateHomeUI(1) }
        llc_set.setOnClickListener { updateHomeUI(2) }
        iv_connect_btn.setOnClickListener {
            connectDisconnectServer()
        }
        llc_contact.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data= Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, Local0914Conf.E)
                startActivity(intent)
            }catch (e: Exception){
                toast("Contact us by email：${Local0914Conf.E}")
            }
        }
        llc_privacy.setOnClickListener {
            startActivity(Intent(this,Url0914Activity::class.java))
        }
        llc_update.setOnClickListener {
            val packName = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=$packName"
                )
            }
            startActivity(intent)
        }
        llc_share.setOnClickListener {
            val pm = packageManager
            val packageName=pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
            startActivity(Intent.createChooser(intent, "share"))
        }
    }

    private fun connectDisconnectServer(){
        if (!home0914CanClick) return
        home0914CanClick=false
        val connected0914 = Connect0914ServerObject.connected0914()
        if (connected0914){
            update0914DisconnectingUI()
            Connect0914ServerObject.disconnect0914Server()
            loop0914CheckResult(false)
        }else{
            updateServerIcon()
            if (getNet0914Status()==1){
                AlertDialog.Builder(this).apply {
                    setMessage("You are not currently connected to the network")
                    setPositiveButton("sure", null)
                    show()
                }
                home0914CanClick=true
                return
            }
            if (VpnService.prepare(this) != null) {
                permission = true
                register0914.launch(null)
                return
            }
            startConnectServer()
        }
    }

    private fun startConnectServer(){
        Connect0914ServerObject.connect0914Server()
        update0914ConnectingUI()
        loop0914CheckResult(true)
    }

    private fun loop0914CheckResult(connect:Boolean){
        this.connectServer=connect
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_CONNECT)
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_RESULT)
        launch0914Job = GlobalScope.launch(Dispatchers.Main) {
            var time = 0
            while (true) {
                if (!isActive) {
                    break
                }
                delay(1000)
                time++
                if (time >= 10) {
                    cancel()
                    connectDisconnectCompleted()
                }
                if (time in 2..9){
                    val success = if (connect) Connect0914ServerObject.connected0914() else Connect0914ServerObject.disconnected0914()
                    if(success){
                        show0914connect.show0914MainAd{
                            cancel()
                            connectDisconnectCompleted(toResult = it)
                        }
                    }
                }
            }
        }
    }

    private fun connectDisconnectCompleted(toResult:Boolean=true){
        val success = if (connectServer) Connect0914ServerObject.connected0914() else Connect0914ServerObject.disconnected0914()
        if (success){
            if (connectServer){
                update0914ConnectedUI()
            }else{
                update0914DisconnectedUI()
                updateServerIcon()
            }
            if (toResult){
                toResult0914Activity()
            }
            home0914CanClick=true
        }else{
            update0914DisconnectedUI()
            toast(if (connectServer) "Connect Fail" else "Disconnect Fail")
            home0914CanClick=true
        }
    }

    private fun toResult0914Activity(){
        if (ActivityCallBack0914.activity0914Front){
            val apply = Intent(this, Result0914Activity::class.java).apply {
                putExtra("state", connectServer)
            }
            startActivity(apply)
        }
    }

    private fun stopLoop0914Check(){
        launch0914Job?.cancel()
        launch0914Job=null
    }

    private fun update0914ConnectingUI(){
        tv_connect_time.isSelected=false
        iv_connect_text_status.setImageResource(R.drawable.connecting_text)
        iv_connect_server_bg.setImageResource(R.drawable.connect_server_bg)
    }

    private fun update0914ConnectedUI(){
        tv_connect_time.isSelected=true
        iv_connect_text_status.setImageResource(R.drawable.disconnect_text)
        iv_connect_server_bg.setImageResource(R.drawable.connected_server_bg)
    }

    private fun update0914DisconnectedUI(){
        tv_connect_time.isSelected=false
        tv_connect_time.text="00:00:00"
        iv_connect_text_status.setImageResource(R.drawable.connect_text)
        iv_connect_server_bg.setImageResource(R.drawable.connect_server_bg)
    }

    private fun update0914DisconnectingUI(){
        tv_connect_time.isSelected=false
        iv_connect_text_status.setImageResource(R.drawable.disconnecting_text)
        iv_connect_server_bg.setImageResource(R.drawable.connect_server_bg)
    }

    private fun updateServerIcon(){
        iv_home_server_icon.setImageResource(serverIcon2(Connect0914ServerObject.server0914En.country_0914_strong))
    }

    private fun updateHomeUI(type:Int,showAd:Boolean=true){
        if (!home0914CanClick) return
        if (showAd){
            if (type==0){
                Load0914AdObject.loadLogic(Ad0914LocationStr.AD_BACK)
            }
            if (preType==0&&type==1){
                preType=type
                show0914back.show0914MainAd{}
                return
            }
        }
        preType=type
        layout_server_list.showView(type==0)
        layout_connect.showView(type==1)
        layout_set.showView(type==2)
        iv_bottom_server.isSelected=type==0
        iv_bottom_connect.isSelected=type==1
        iv_bottom_set.isSelected=type==2
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCallBack0914.refresh0914HomeAd){
            show0914Home.loop0914Call()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLoop0914Check()
        show0914Home.stopLaunch0914Job()
        Connect0914ServerObject.onDestroy()
        ActivityCallBack0914.refresh0914HomeAd=true
        Connect0914TimeObject.deleteCallback(this)
    }

    override fun disconnectedCallback() {
        if (home0914CanClick){
            update0914DisconnectedUI()
        }
    }

    override fun connectedCallback() {
        update0914ConnectedUI()
    }

    override fun connect0914Time(time: String) {
        tv_connect_time.text=time
    }
}
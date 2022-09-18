package com.demo.strongbox.activity0914

import android.os.Bundle
import com.demo.strongbox.R
import com.demo.strongbox.fire.Local0914Conf
import kotlinx.android.synthetic.main.activity_url0914.*

class Url0914Activity:Base0914Activity(R.layout.activity_url0914) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iv_back.setOnClickListener { finish() }
        webview.apply {
            settings.javaScriptEnabled=true
            loadUrl(Local0914Conf.U)
        }
    }
}
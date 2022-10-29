package com.demo.strongbox.activity0914

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.demo.strongbox.R
import com.gyf.immersionbar.ImmersionBar

abstract class Base0914Activity(private val id:Int) : AppCompatActivity(){
    var resume0914=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        des()
        setContentView(id)
        ImmersionBar.with(this).apply {
            fitsSystemWindows(true)
            statusBarColor(R.color.color_7591d5)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
            init()
        }
    }

    private fun des(){
        val metrics: DisplayMetrics = resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }

    override fun onResume() {
        super.onResume()
        resume0914=true
    }

    override fun onPause() {
        super.onPause()
        resume0914=false
    }

    override fun onStop() {
        super.onStop()
        resume0914=false
    }

}
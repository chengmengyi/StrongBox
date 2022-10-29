package com.demo.strongbox.activity0914


import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.strongbox.R
import com.demo.strongbox.ad0914.Ad0914LocationStr
import com.demo.strongbox.ad0914.Load0914AdObject
import com.demo.strongbox.ad0914.Show0914MainObject
import kotlinx.android.synthetic.main.activity_main.*

class Main0914Activity : Base0914Activity(R.layout.activity_main) {
    private var apply:ValueAnimator?=null
    private val showMain by lazy { Show0914MainObject(this,Ad0914LocationStr.AD_MAIN) {
        animatorFinish()
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_MAIN)
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_HOME)
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_CONNECT)
        Load0914AdObject.loadLogic(Ad0914LocationStr.AD_RESULT)

        apply = ValueAnimator.ofInt(0, 100).apply {
            duration = 10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                progress_main.progress = progress
                val a = (10 * (progress / 100.0F)).toInt()
                if (a in 2..9){
                    showMain.show0914MainAd{
                        stopAnimator()
                        progress_main.progress = 100
                        if (it){
                            animatorFinish()
                        }
                    }
                }else if (a>=10){
                    animatorFinish()
                }
            }
            start()
        }
    }

    private fun animatorFinish(){
        if (!ActivityUtils.isActivityExistsInStack(Home0914Activity::class.java)){
            startActivity(Intent(this,Home0914Activity::class.java))
        }
        finish()
    }

    override fun onResume() {
        super.onResume()
        apply?.resume()
    }

    override fun onPause() {
        super.onPause()
        apply?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
    }

    private fun stopAnimator(){
        apply?.removeAllUpdateListeners()
        apply?.cancel()
        apply=null
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }
}
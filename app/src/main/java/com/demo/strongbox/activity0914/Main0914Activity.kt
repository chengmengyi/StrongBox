package com.demo.strongbox.activity0914


import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.strongbox.R
import kotlinx.android.synthetic.main.activity_main.*

class Main0914Activity : Base0914Activity(R.layout.activity_main) {
    private var apply:ValueAnimator?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apply = ValueAnimator.ofInt(0, 100).apply {
            duration = 3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                progress_main.progress = progress
            }
            doOnEnd {
                animatorFinish()
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
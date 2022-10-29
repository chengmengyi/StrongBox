package com.demo.strongbox.ad0914

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.demo.strongbox.R
import com.demo.strongbox.activity0914.Base0914Activity
import com.demo.strongbox.app.ActivityCallBack0914
import com.demo.strongbox.app.Strong
import com.demo.strongbox.app.printStrong
import com.demo.strongbox.app.showView
import com.demo.strongbox.fire.Read0914Firebase
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

abstract class BaseShow0914HomeAd(
    private val t:String,
    private val base0914Activity: Base0914Activity
    ) {

    protected fun show(nativeAd: NativeAd){
        printStrong("show $t ad")
        val findViewById = base0914Activity.findViewById<NativeAdView>(R.id.nv_ad0914_view)

        findViewById.mediaView=base0914Activity.findViewById(R.id.iv_ad0914_media)
        if (null!=nativeAd.mediaContent){
            findViewById.mediaView?.apply {
                setMediaContent(nativeAd.mediaContent)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        if (view == null || outline == null) return
                        outline.setRoundRect(
                            0,
                            0,
                            view.width,
                            view.height,
                            SizeUtils.dp2px(10F).toFloat()
                        )
                        view.clipToOutline = true
                    }
                }
            }
        }
        findViewById.headlineView=base0914Activity.findViewById(R.id.tv_ad0914_title)
        (findViewById.headlineView as AppCompatTextView).text=nativeAd.headline

        findViewById.bodyView=base0914Activity.findViewById(R.id.tv_id0914_desc)
        (findViewById.bodyView as AppCompatTextView).text=nativeAd.body

        findViewById.iconView=base0914Activity.findViewById(R.id.iv_ad0914_logo)
        (findViewById.iconView as ImageFilterView).setImageDrawable(nativeAd.icon?.drawable)

        findViewById.callToActionView=base0914Activity.findViewById(R.id.tv_ad0914_install)
        (findViewById.callToActionView as AppCompatTextView).text=nativeAd.callToAction

        findViewById.setNativeAd(nativeAd)
        Read0914Firebase.addShow0914Num()
        base0914Activity.findViewById<AppCompatImageView>(R.id.iv_ad0914_cover).showView(false)

        showNativeAdCompleted()
    }

    private fun showNativeAdCompleted(){
        if (t==Ad0914LocationStr.AD_HOME){
            ActivityCallBack0914.refresh0914HomeAd=false
        }
        Load0914AdObject.removeAd(t)
        Load0914AdObject.loadLogic(t)
    }
}
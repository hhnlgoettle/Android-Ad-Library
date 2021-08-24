package engineer.trustmeimansoftware.adlib.ad

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.AdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback

/**
 * @class FullscreenAd
 *
 * an Ad that is displayed in a WebView in fullscreen
 */
open class FullscreenAd(id: String, url: String) : Ad(id, url) {

    var fullscreenContentCallback: FullscreenContentCallback? = null

    /**
     * show
     *
     * display the ad
     */
    open fun show(activity: AppCompatActivity) {
        if(fullscreenContentCallback == null) {
            throw Error("fullscreenContentCallback not set")
        }
        val intent = AdManager.instance?.adFullscreenActivityBuilder?.buildIntent(activity)
        AdManager.instance?.adRegistry?.add(this)
        intent?.putExtra("EXTRA_AD_ID", this.getID())
        Log.d("FullscreenAd", "show(): intent: $intent")
        intent?.let {
            AdManager.instance?.adFullscreenActivityBuilder?.launchIntent(it)
        }
    }

    companion object {
        // static inheritance is disallowed in kotlin
        // https://stackoverflow.com/questions/39303180/kotlin-how-can-i-create-a-static-inheritable-function
        fun load(activity: AppCompatActivity, adRequest: AdRequest, adLoadCallback: AdLoadCallback) {
            if(adRequest.type == null) adRequest.type = this::class.java.declaringClass.toString()
            Ad.load(activity, adRequest, adLoadCallback)
        }
    }

}
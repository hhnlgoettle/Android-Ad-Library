package engineer.trustmeimansoftware.adlib.ad

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback

/**
 * models an Ad that is displayed in fullscreen mode
 */
open class FullscreenAd(id: String, url: String, requestResult: AdRequestResult? = null) : Ad(id, url, requestResult) {

    var fullscreenContentCallback: FullscreenContentCallback? = null

    /**
     * shows the ad
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
        /**
         * loads the ad
         */
        fun load(activity: AppCompatActivity, adRequest: AdRequest, adLoadCallback: AdLoadCallback) {
            if(adRequest.type == null) adRequest.type = this::class.java.declaringClass.toString()
            Ad.load(activity, adRequest, adLoadCallback)
        }
    }

}
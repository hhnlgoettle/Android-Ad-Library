package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivity
import engineer.trustmeimansoftware.adlib.reward.RewardItem
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

/**
 * @class AdFullscreenActivity
 *
 * An Activity that displays a FullscreenAd
 */
open class TestWebviewActivity : AppCompatActivity(), IAdFullscreenActivity {
    var adID: String? = null
    var ad: InteractionRewardedAd? = null
    private val onFinishData = Intent()
    var resultCode = RESULT_OK
    var webviewClient: WebViewClient? = null;

    lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testwebview)
        AdManager.build(this)
        webview = findViewById(R.id.webview)
    }

    @SuppressLint("LongLogTag")
    fun executeInWebview(script: String) {
        this.webview.evaluateJavascript(script) {
            Log.d("TestAdFullscreenActivity", "Result from callback: $it")
        };
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun showAd() {
        ad?.let {
            // Force links and redirects to open in the WebView instead of in a browser
            webview.webChromeClient = WebChromeClient()
            webview.webViewClient = WebViewClient()
            webviewClient?.let {
                webview.webViewClient = it
            }
            val webSettings: WebSettings = webview.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.allowFileAccess = true
            webSettings.allowUniversalAccessFromFileURLs = true

            // allow auto play of video elements
            // https://stackoverflow.com/questions/38975229/auto-play-video-in-webview
            webview.settings.mediaPlaybackRequiresUserGesture = false

            if (AdManager.instance?.cacheManager?.existsAdForActivity(this, it.getID()) != true) {
                ad?.fullscreenContentCallback?.onFailedToShow(Error("ad creative does not exists at path: " + it.path))
                finishActivityWithError(Error("ad creative does not exists at path: " + it.path))
                return
            }
            val jsInterface = AdManager.instance!!.jsInterfaceBuilder!!.build(this)
            webview.addJavascriptInterface(jsInterface, "AndroidIRA")
            webview.loadUrl(it.path)
        }
    }

    /**
     *
     */
    override fun finishActivity(stats: ImpressionStats?) {
        try {
            ad?.fullscreenContentCallback?.onDismissed()
            // TODO send stats
            // TODO generate additional reward items based on interactions
            stats?.let {
                if(it.hasEarnedReward) {
                    ad?.onUserRewardedListener?.onRewardEarned(arrayOf(RewardItem(ad?.rewardType!!, ad?.rewardAmount!!)))
                }
            }
            this.cleanupWebview()
            onFinishData.putExtra("EXTRA_AD_ID", adID)
            setResult(resultCode, onFinishData)
        } finally {
            this.finish()
        }
    }

    /**
     * closes the activity with an error
     * @param error - the cause
     */
    override fun finishActivityWithError(error: java.lang.Error, stats: ImpressionStats?) {
        try {
            this.cleanupWebview()
            onFinishData.putExtra("EXTRA_ERROR_MESSAGE", error.message)
            resultCode = RESULT_CANCELED
        } finally {
            this.finishActivity(stats)
        }
    }

    /**
     * destroys the webview
     */
    private fun cleanupWebview() {
        try {
            webview.removeAllViews()
            webview.destroy()
        } catch (e: Exception) {
        }
    }
}
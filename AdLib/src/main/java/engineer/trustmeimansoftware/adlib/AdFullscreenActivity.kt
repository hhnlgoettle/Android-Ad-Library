package engineer.trustmeimansoftware.adlib

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.reward.RewardItem
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

/**
 * @class AdFullscreenActivity
 *
 * An Activity that displays a FullscreenAd
 */
open class AdFullscreenActivity : AppCompatActivity(), IAdFullscreenActivity {
    private var adID: String? = null
    private var ad: InteractionRewardedAd? = null
    private val onFinishData = Intent()
    private var resultCode = RESULT_OK

    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adfullscreen)
        
        webview = findViewById(R.id.webview)

        getAdFromIntent()
        showAd()
    }

    /**
     * getAdFromIntent
     *
     * loads adID from the intent
     */
    private fun getAdFromIntent() {
        adID = intent.extras?.getString("EXTRA_AD_ID")
        if (adID == null || adID!!.isEmpty()) {
            finishActivityWithError(Error("adID is not set. Did you pass the ID in the Intent's Extras as EXTRA_AD_ID?"))
        }
        adID?.let {
            val adFromRegistry = AdManager.instance?.adRegistry?.get(it)
            if (adFromRegistry == null) {
                finishActivityWithError(Error("ad is null"))
            }
            if (adFromRegistry is InteractionRewardedAd) {
                ad = adFromRegistry
            } else {
                finishActivityWithError(Error("ad is not a InteractionRewardedAd"))
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showAd() {
        ad?.let {
            val webSettings: WebSettings = webview.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true

            // Force links and redirects to open in the WebView instead of in a browser

            // Force links and redirects to open in the WebView instead of in a browser
            webview.webViewClient = WebViewClient()
            WebView.setWebContentsDebuggingEnabled(true);
            // allow auto play of video elements
            // https://stackoverflow.com/questions/38975229/auto-play-video-in-webview
            webview.settings.mediaPlaybackRequiresUserGesture = false

            if (AdManager.instance?.cacheManager?.existsAdForActivity(this, it.getID()) != true) {
                ad?.fullscreenContentCallback?.onFailedToShow(Error("ad creative does not exists at path: " + it.path))
                finishActivityWithError(Error("ad creative does not exists at path: " + it.path))
                return
            }

            // build js Interface
            val jsInterface = AdManager.instance!!.jsInterfaceBuilder!!.build(this)
            // if ad has additionalReward set, we can init the LiveReward Object
            // LiveReward will display the user how much they have earned, which will be lost,
            // when they close the ad prematurely
            ad?.additionalReward?.let {
                reward ->
                jsInterface.onStartFunc = {
                    this.runOnUiThread {
                        webview.evaluateJavascript("InteractionRewardingAds.initLiveReward({rewardAmount: ${reward.amount.toInt()}});") {};
                    }
                }
            }

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
            stats?.let {
                if(it.hasEarnedReward) {
                    val rewards = ArrayList<RewardItem>()
                    rewards.add(RewardItem(ad?.rewardType!!, ad?.rewardAmount!!))
                    it.rewardPercentage?.let { rewardPercentage ->
                        rewards.add(RewardItem("additionalReward", rewardPercentage.toLong()))
                    }
                    ad?.onUserRewardedListener?.onRewardEarned(rewards.toTypedArray())
                }
                AdManager.instance?.networkManager?.sendImpressionStats(ad, stats)
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
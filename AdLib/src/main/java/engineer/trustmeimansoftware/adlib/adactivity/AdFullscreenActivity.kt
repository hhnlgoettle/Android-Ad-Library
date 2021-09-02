package engineer.trustmeimansoftware.adlib.adactivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import engineer.trustmeimansoftware.adlib.R
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.reward.RewardItem
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * Activity that handles the display of an InteractionRewardedAd
 * <p>An instance of this class will on creation look up
 */
open class AdFullscreenActivity : AppCompatActivity(), IAdFullscreenActivity {
    /**
     * the extracted adID from the savedInstanceBundle
     */
    private var adID: String? = null

    /**
     * the ad found in the [engineer.trustmeimansoftware.adlib.registry.AdRegistry] instance
     */
    private var ad: InteractionRewardedAd? = null

    /**
     * return object returned to the activity, that created this activity
     */
    private val onFinishData = Intent()

    /**
     * result code
     */
    private var resultCode = RESULT_OK

    /**
     * the webview that displays the creative
     */
    private lateinit var webview: WebView

    /**
     * the LinearLayout that displays the info
     */
    private lateinit var infoLayout: LinearLayout

    /**
     * progress bar during info screen
     */
    private lateinit var infoProgressBar: ProgressBar

    /**
     * should textview inform about interaction rewarding ads at the beginning
     */
    private var showInfoOnStart: Boolean = true

    /**
     * how long info text is present before showing ad
     */
    private var infoTimeInMillis: Long = 3500

    /**
     * flag to check if ad has started
     * used to prevent multiple loading of ad
     */
    private var hasAdStarted: Boolean = false


    /**
     *
     * @param savedInstanceState the [Bundle] that contains the adID under key <b>EXTRA_AD_ID</b>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adfullscreen)
        
        webview = findViewById(R.id.webview)
        infoLayout = findViewById(R.id.infoLayout)
        infoProgressBar = findViewById(R.id.progressBar)

        getConfigValues()
        getAdFromIntent()
        if(showInfoOnStart) {
            showInfo()
        } else {
            showAd()
        }
    }

    /**
     * loads [showInfoOnStart] and [infoTimeInMillis] from [AdManager] 's config if present
     */
    private fun getConfigValues() {
        val manager = AdManager.instance
        manager?.config?.let {
            showInfoOnStart = it.showInfoTextPreAdDisplay
            infoTimeInMillis = it.infoTextDisplayTimeInMillis
        }
    }

    /**
     * extracts adID from intent and loads ad from adRegistry
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

    /**
     * shows the ad in webview
     * and attaches an JavaScriptListener under key <b>AndroidIRA</b> to the webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun showAd() {
        // prevent loading ad multiple times
        if(hasAdStarted) return
        hasAdStarted = true

        // make webview visible, infoLayout invisible
        infoLayout.visibility = View.GONE
        webview.visibility = View.VISIBLE

        // load ad
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
                        webview.evaluateJavascript("InteractionRewardingAds.initLiveReward({rewardAmount: ${reward.amount.toInt()}, rewardType: \"${reward.type}\"});") {};
                    }
                }
            }

            webview.addJavascriptInterface(jsInterface, "AndroidIRA")
            webview.loadUrl(it.path)
        }
    }

    /**
     * shows an info text about interaction rewarding ads for a short timespan
     * then calls [showAd]
     *
     */
    private fun showInfo() {
        infoLayout.visibility = View.VISIBLE
        webview.visibility = View.INVISIBLE
        Timer("DelayedShowAd", false).schedule(object: TimerTask() {
            override fun run() {
                // need to run on ui thread because we touch views
                runOnUiThread {
                    showAd()
                }
            }
        }, infoTimeInMillis)
        startInfoProgressBarUpdate()
    }

    /**
     * starts a coroutine that updates the [infoProgressBar]
     * stops when progress reaches 100 or parent layout is not visible anymore
     */
    private fun startInfoProgressBarUpdate() {
        val scope = CoroutineScope(Dispatchers.Default)
        val timeUnits = 100
        scope.launch {
            while(infoProgressBar.progress < 100 && infoLayout.visibility == View.VISIBLE) {
                Log.d("ProgressBar", "update: "+infoProgressBar.progress.toString())
                try {
                    delay(infoTimeInMillis / timeUnits)
                } catch (e: Exception) {
                }
                infoProgressBar.incrementProgressBy(100 / timeUnits)
            }
        }
    }

    /**
     * finishes the activity
     * <p>if stats is not null, it is sent to the server
     * @param stats the ImpressionStats collected during the impression
     *
     */
    override fun finishActivity(stats: ImpressionStats?) {
        try {
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
            ad?.fullscreenContentCallback?.onDismissed()
            this.cleanupWebview()
            onFinishData.putExtra("EXTRA_AD_ID", adID)
            setResult(resultCode, onFinishData)
        } finally {
            this.finish()
        }
    }

    /**
     * finishes the activity with an error code
     * <p>Puts the [Error] 's message in the [onFinishData] under key <b>EXTRA_ERROR_MESSAGE</b>
     * @param error the cause
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
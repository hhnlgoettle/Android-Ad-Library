package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.IAdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback

class TestActivity : AppCompatActivity() {

    companion object {
        // use TestAdFullscreenActivityBuilder instead of AdFullscreenActivityBuilder
        const val optCustomAdFullscreenBuilder = "EXTRA_CUSTOM_AD_FULLSCREEN_BUILDER"
        const val optCustomJSInterfaceBuilder = "EXTRA_CUSTOM_JS_BUILDER"
        const val optCustomNetworkManager = "EXTRA_CUSTOM_NETWORK_MANAGER"
        const val TAG ="TestActivity"
    }

    var adManager: IAdManager? = null
    var ad: InteractionRewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        AdManager.build(this)
        adManager = AdManager.instance!!
        updateAdManager()
    }

    private fun updateAdManager() {
        intent.let {
            if(it.extras?.getBoolean(optCustomAdFullscreenBuilder) == true) {
                Log.d(TAG, "use custom AdFullscreenBuilder")
                val builder = TestAdFullscreenActivityBuilder(this)
                AdManager.instance?.adFullscreenActivityBuilder = builder
            }
            if(it.extras?.getBoolean(optCustomJSInterfaceBuilder) == true) {
                Log.d(TAG, "use custom JSInterfaceBuilder")
                val jsBuilder = TestJavaScriptInterfaceBuilder()
                AdManager.instance?.jsInterfaceBuilder = jsBuilder
            }
            if(it.extras?.getBoolean(optCustomNetworkManager) == true) {
                Log.d(TAG, "use custom NetworkManager")
                val networkManager = TestAdNetworkManager()
                AdManager.instance?.networkManager = networkManager
            }

        }
    }

    fun loadAd(loadCallback: AdLoadCallback) {
        val adRequest = AdRequest("publisherID", "displayID", arrayOf<String>())
        InteractionRewardedAd.load(this, adRequest, loadCallback)
    }

    fun displayAd() {
        if(ad == null) return
        ad!!.show(this)
    }
}
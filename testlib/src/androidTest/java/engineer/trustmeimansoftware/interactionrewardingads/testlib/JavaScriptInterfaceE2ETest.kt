package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import org.junit.Test
import java.util.concurrent.CountDownLatch
import junit.framework.TestCase.assertEquals

class JavaScriptInterfaceE2ETest {
    @Test(timeout = 8000)
    fun e2e_test_close_api() {
        val latch = CountDownLatch(1)

        val scenarioIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            TestWebviewActivity::class.java
        )
        val scenario: ActivityScenario<TestWebviewActivity> =
            ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            AdManager.instance!!.cacheManager = OfflineCacheManager();
            val adManager = AdManagerUtil(AdManager.instance!!)
            val jsBuilder = TestJavaScriptInterfaceBuilder()
            jsBuilder.onCloseCb = { _: String ->
                latch.countDown()
            }

            AdManager.instance!!.jsInterfaceBuilder = jsBuilder;
            val adID = "e2e"
            activity.ad = InteractionRewardedAd(adID, adManager.cache.getFullCreativePath(adID))
            activity.adID = adID;

            activity.webviewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    view!!.evaluateJavascript("InteractionRewardingAds.init({});InteractionRewardingAds.close();") {};
                }
            }
            activity.showAd();
        }

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Test(timeout = 4000)
    fun e2e_test_onClose_result() {
        val latch = CountDownLatch(1)
        var resultAsJSON:String? = "";

        val scenarioIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            TestWebviewActivity::class.java
        )
        val scenario: ActivityScenario<TestWebviewActivity> =
            ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            AdManager.instance!!.cacheManager = OfflineCacheManager();
            val adManager = AdManagerUtil(AdManager.instance!!)
            val jsBuilder = TestJavaScriptInterfaceBuilder()
            jsBuilder.onCloseCb = { result: String ->
                resultAsJSON = result;
                latch.countDown()
            }

            AdManager.instance!!.jsInterfaceBuilder = jsBuilder;
            val adID = "e2e"
            activity.ad = InteractionRewardedAd(adID, adManager.cache.getFullCreativePath(adID))
            activity.adID = adID;

            activity.webviewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    view!!.evaluateJavascript("InteractionRewardingAds.init({desiredDurationMillis: 15000,desiredInteractionCount: 12 });") {};
                    Thread.sleep(1000)
                    view.evaluateJavascript("InteractionRewardingAds.close()") {};
                }
            }
            activity.showAd();
        }

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val stats = ImpressionStats.fromJSON(resultAsJSON!!)
        assertEquals(15000L, stats.desiredDuration)
        assertEquals(12L, stats.desiredInteractionCount)
        assertEquals(false, stats.hasEarnedReward)
        assertEquals(0, stats.validInteractions?.size)
        assertEquals(0, stats.allInteractions?.size)
    }

    @Test(timeout = 4000)
    fun e2e_test_onClose_result_reward_earned() {
        val latch = CountDownLatch(1)
        var resultAsJSON:String? = "";

        val scenarioIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            TestWebviewActivity::class.java
        )
        val scenario: ActivityScenario<TestWebviewActivity> =
            ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            AdManager.instance!!.cacheManager = OfflineCacheManager();
            val adManager = AdManagerUtil(AdManager.instance!!)
            val jsBuilder = TestJavaScriptInterfaceBuilder()
            jsBuilder.onCloseCb = { result: String ->
                resultAsJSON = result;
                latch.countDown()
            }

            AdManager.instance!!.jsInterfaceBuilder = jsBuilder;
            val adID = "e2e"
            activity.ad = InteractionRewardedAd(adID, adManager.cache.getFullCreativePath(adID))
            activity.adID = adID;

            activity.webviewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    view!!.evaluateJavascript("InteractionRewardingAds.init({desiredDurationMillis: 1000,desiredInteractionCount: 4 });") {};
                    view.evaluateJavascript("document.getElementById('valid-div').click()") {};
                    view.evaluateJavascript("InteractionRewardingAds.timer.on('onCountdownIsZero', function() {InteractionRewardingAds.close()})") {};
                }
            }
            activity.showAd();
        }

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val stats = ImpressionStats.fromJSON(resultAsJSON!!)
        assertEquals(1000L, stats.desiredDuration)
        assertEquals(4L, stats.desiredInteractionCount)
        assertEquals(true, stats.hasEarnedReward)
        assertEquals(25, stats.rewardPercentage)
        assertEquals(1, stats.validInteractions?.size)
        assertEquals(1, stats.allInteractions?.size)
    }
}
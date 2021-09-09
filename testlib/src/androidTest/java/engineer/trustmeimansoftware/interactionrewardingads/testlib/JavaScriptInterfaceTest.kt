package engineer.trustmeimansoftware.interactionrewardingads.testlib


import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.Setup
import org.junit.Assert.*
import org.junit.Test
import java.lang.Error
import java.util.concurrent.CountDownLatch

/**
 * @class JavaScriptInterfaceTest
 *
 * use an modified index.html to trigger calls to interface methods
 */
class JavaScriptInterfaceTest {
    @Test(timeout = 1000)
    fun testInterface_onStart() {

        var called = false
        val latch = CountDownLatch(1)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf(
                TestActivity.optCustomAdFullscreenBuilder,
                TestActivity.optCustomJSInterfaceBuilder,
                TestActivity.optCustomNetworkManager,
                TestActivity.optOfflineCacheManager
            )
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->

            val jsBuilder: TestJavaScriptInterfaceBuilder =
                (activity.adManager?.jsInterfaceBuilder as TestJavaScriptInterfaceBuilder?)!!
            jsBuilder.onStartCb = {
                called = true
                latch.countDown()
            }

            val builder: TestAdFullscreenActivityBuilder =
                (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!

            val ad = InteractionRewardedAd(
                "onStart",
                AdManager.instance?.cacheManager?.getFullCreativePath("onStart")!!
            )
            AdManager.instance?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }

    @Test(timeout = 1000)
    fun testInterface_onClose() {

        var called = false
        val latch = CountDownLatch(1)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf(
                TestActivity.optCustomAdFullscreenBuilder,
                TestActivity.optCustomJSInterfaceBuilder,
                TestActivity.optOfflineCacheManager,
            )
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val jsBuilder = (activity.adManager?.jsInterfaceBuilder as TestJavaScriptInterfaceBuilder?)!!
            jsBuilder.onCloseCb = { _ ->
                called = true
                latch.countDown()
            }

            val builder: TestAdFullscreenActivityBuilder =
                (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!

            val id = "onClose"
            val ad = InteractionRewardedAd(
                id,
                AdManager.instance?.cacheManager?.getFullCreativePath(id)!!
            )
            AdManager.instance?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }

    @Test(timeout = 1000)
    fun testInterface_onCloseOnError() {

        var called = false
        val latch = CountDownLatch(1)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf(
                TestActivity.optCustomAdFullscreenBuilder,
                TestActivity.optCustomJSInterfaceBuilder,
                TestActivity.optOfflineCacheManager
            )
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->

            val jsBuilder = (activity.adManager?.jsInterfaceBuilder as TestJavaScriptInterfaceBuilder?)!!
            jsBuilder.onCloseOnErrorCb = { _, _ ->
                called = true
                latch.countDown()
            }

            val builder =
                (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!

            val id = "onCloseOnError"
            val ad = InteractionRewardedAd(
                id,
                AdManager.instance?.cacheManager?.getFullCreativePath(id)!!
            )
            AdManager.instance?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }

    @Test(timeout = 3000)
    fun interface_onClose_closesActivity() {

        var called = false
        val latch = CountDownLatch(1)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf(
                TestActivity.optCustomAdFullscreenBuilder,
                TestActivity.optCustomNetworkManager,
                TestActivity.optOfflineCacheManager
            )
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->

            AdManager.instance!!.cacheManager = OfflineCacheManager();
            activity.adManager = AdManager.instance!!

            val builder =
                (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!

            builder.onResultCallback = {
                assertEquals(null, it.data?.getStringExtra("EXTRA_ERROR_MESSAGE"))
                assertEquals(AppCompatActivity.RESULT_OK, it.resultCode)
                latch.countDown()
            }

            val id = "onClose"
            val ad = InteractionRewardedAd(
                id,
                AdManager.instance?.cacheManager?.getFullCreativePath(id)!!
            )
            ad.fullscreenContentCallback = object: FullscreenContentCallback {
                override fun onDismissed() {
                    called = true
                    latch.countDown()
                }
                override fun onFailedToShow(error: Error) {
                }
            }
            AdManager.instance?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }

    @Test(timeout = 2000)
    fun interface_onCloseOnError_closesActivity() {

        var called = false
        val latch = CountDownLatch(2)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf(
                TestActivity.optCustomAdFullscreenBuilder,
                TestActivity.optOfflineCacheManager,
                TestActivity.optCustomNetworkManager
            )
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->

            val builder =
                (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!

            builder.onResultCallback = {
                assertEquals(AppCompatActivity.RESULT_CANCELED, it.resultCode)
                latch.countDown()
            }

            val id = "onCloseOnError"
            val ad = InteractionRewardedAd(
                id,
                AdManager.instance?.cacheManager?.getFullCreativePath(id)!!
            )
            ad.fullscreenContentCallback = object: FullscreenContentCallback {
                override fun onDismissed() {
                    called = true
                    latch.countDown()
                }
                override fun onFailedToShow(error: Error) {
                }
            }
            AdManager.instance?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }


}
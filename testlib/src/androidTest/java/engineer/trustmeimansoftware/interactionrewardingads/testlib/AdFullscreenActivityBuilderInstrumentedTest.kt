package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.adactivity.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.ad.FullscreenAd
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AdFullscreenActivityBuilderInstrumentedTest {
    // https://stackoverflow.com/a/32827600/16001266
    @get: Rule
    var rule: ActivityScenarioRule<*> = ActivityScenarioRule(AdFullscreenActivity::class.java)

    private fun setupScenarioIntent(options: Array<String>? = null): Intent {
        val scenarioIntent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, TestActivity::class.java)
        val myBundle = Bundle()
        options?.let {
            for (i in options.indices) {
                myBundle.putBoolean(options[i], true)
            }
        }
        scenarioIntent.putExtras(myBundle)
        return scenarioIntent
    }

    @Test
    fun testBuilder_throwsErrorAdIsNull() {
        val scenarioIntent = setupScenarioIntent(arrayOf(TestActivity.optCustomAdFullscreenBuilder))

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val builder: TestAdFullscreenActivityBuilder = (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!
            builder.onResultCallback = {
                assertEquals(AppCompatActivity.RESULT_CANCELED, it.resultCode)
                assertEquals("ad is null",
                    it.data?.extras?.getString("EXTRA_ERROR_MESSAGE")
                )
            }
            val intent = builder.buildIntent(activity)
            val bundle = Bundle()
            bundle.putString("EXTRA_AD_ID", "myAd")
            intent.putExtras(bundle)
            builder.launchIntent(intent)
        }
    }

    @Test
    fun testBuilder_throwsError_CreativeDoesNotExistsAtPath() {
        val scenarioIntent = setupScenarioIntent(arrayOf(TestActivity.optCustomAdFullscreenBuilder))

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val builder: TestAdFullscreenActivityBuilder = (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!
            builder.onResultCallback = {
                assertEquals(AppCompatActivity.RESULT_CANCELED, it.resultCode)
                assertEquals("ad creative does not exists at path: someUrl",
                    it.data?.extras?.getString("EXTRA_ERROR_MESSAGE")
                )
            }
            val ad = InteractionRewardedAd("myAdId", "someUrl")
            activity.adManager?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
    }

    @Test
    fun testBuilder_throwsError_AdIsNotInteractionRewardedAd() {
        val scenarioIntent = setupScenarioIntent(arrayOf(TestActivity.optCustomAdFullscreenBuilder))

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val builder: TestAdFullscreenActivityBuilder = (activity.adManager?.adFullscreenActivityBuilder as TestAdFullscreenActivityBuilder?)!!
            builder.onResultCallback = {
                assertEquals(AppCompatActivity.RESULT_CANCELED, it.resultCode)
                assertEquals("ad is not a InteractionRewardedAd",
                    it.data?.extras?.getString("EXTRA_ERROR_MESSAGE")
                )
            }
            val ad = FullscreenAd("myAdId", "someUrl")
            activity.adManager?.adRegistry?.add(ad)
            val intent = builder.buildIntent(activity, ad)
            builder.launchIntent(intent)
        }
    }



}
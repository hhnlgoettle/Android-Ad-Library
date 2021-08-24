package engineer.trustmeimansoftware.adlib

import android.content.Intent
import android.os.Bundle

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import engineer.trustmeimansoftware.adlib.ad.FullscreenAd
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Error

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AdFullscreenActivityInstrumentedTest {

    // https://stackoverflow.com/a/32827600/16001266
    @get: Rule
    var rule: ActivityScenarioRule<*> = ActivityScenarioRule(AdFullscreenActivity::class.java)


    @Test
    fun launchActivityWithNoID_throwsError() {
        val scenario = ActivityScenario.launch(AdFullscreenActivity::class.java)
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_ERROR_MESSAGE"),
            "adID is not set. Did you pass the ID in the Intent's Extras as EXTRA_AD_ID?"
        )
    }

    @Test
    fun launchActivityWithWithAdID() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), AdFullscreenActivity::class.java)
        val bundle = Bundle()
        bundle.putString("EXTRA_AD_ID", "someAdID")
        intent.putExtras(bundle)
        val scenario = ActivityScenario.launch<AdFullscreenActivity>(intent)
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_ERROR_MESSAGE"),
            "ad is null"
        )
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_AD_ID"),
            "someAdID"
        )
    }

    @Test
    fun launchActivityWithInvalidAdID() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), AdFullscreenActivity::class.java)
        val bundle = Bundle()
        bundle.putString("EXTRA_AD_ID", "someAdID")
        intent.putExtras(bundle)
        val scenario = ActivityScenario.launch<AdFullscreenActivity>(intent)
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_ERROR_MESSAGE"),
            "ad is null"
        )
    }

    @Test
    fun launchActivityWithAdManagerAndWrongAdType_throwsError() {
        // activity is only needed if we want to create an activity through the adManager
        AdManager.build(null)
        val ad = FullscreenAd("myID", "someURL")
        AdManager.instance?.adRegistry?.add(ad)
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), AdFullscreenActivity::class.java)
        val bundle = Bundle()
        bundle.putString("EXTRA_AD_ID", "myID")
        intent.putExtras(bundle)
        val scenario = ActivityScenario.launch<AdFullscreenActivity>(intent)
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_ERROR_MESSAGE"),
            "ad is not a InteractionRewardedAd"
        )
    }

    @Test
    fun launchActivityWithAdManagerAndAdWithInvalidPath_throwsError() {
        // activity is only needed if we want to create an activity through the adManager
        AdManager.build(null)
        val ad = InteractionRewardedAd("myID", "someURL")

        var callbackCalled = false

        ad.fullscreenContentCallback = object : FullscreenContentCallback {
            override fun onDismissed() {
            }

            override fun onFailedToShow(error: Error) {
                assertEquals(error.message, "ad creative does not exists at path: someURL")
                callbackCalled = true
            }
        }
        AdManager.instance?.adRegistry?.add(ad)
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), AdFullscreenActivity::class.java)
        val bundle = Bundle()
        bundle.putString("EXTRA_AD_ID", "myID")
        intent.putExtras(bundle)
        val scenario = ActivityScenario.launch<AdFullscreenActivity>(intent)
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_ERROR_MESSAGE"),
            "ad creative does not exists at path: someURL"
        )
        assertEquals(
            scenario.result.resultData.getStringExtra("EXTRA_AD_ID"),
            "myID"
        )
        assertEquals(callbackCalled, true)
    }
}

package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.Setup
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Error

@RunWith(AndroidJUnit4::class)
class InteractionRewardingAdTest {
    @Test
    fun loadAd() {
        // Context of the app under test.
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )
        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            activity.adManager?.networkManager = OfflineAdNetworkManager()

            val possibleAdIDs = activity.adManager?.cacheManager?.getCachedAdIDs()!!

            InteractionRewardedAd.load(
                activity,
                AdRequest.build("someDisplayID"),
                object : AdLoadCallback {
                    override fun onAdLoaded(ad: Ad) {
                        assertEquals(true, ad is InteractionRewardedAd)
                        assertEquals(true, possibleAdIDs.contains(ad.getID()))
                        assertEquals(true, ad.getID().isNotEmpty())
                    }

                    override fun onAdFailedToLoad(error: Error) {
                    }
                })
        }


    }
}
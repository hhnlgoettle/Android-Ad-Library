package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheManagerTest {
    @Test
    fun getCachedAds() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AdManager.build(null);
        AdManager.instance?.context = appContext
        AdManager.instance?.networkManager = OfflineAdNetworkManager()

        val manager = AdManagerUtil(AdManager.instance!!)

        val cachedAds = manager.cache.getCachedAdIDs();
        assertEquals(true, cachedAds.size > 0)
    }
}
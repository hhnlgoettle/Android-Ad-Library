package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdRequestTest {
    @Test
    fun build() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AdManager.build(null);
        AdManager.instance?.context = appContext
        AdManager.instance?.networkManager = OfflineAdNetworkManager()
        AdManager.instance?.cacheManager = OfflineCacheManager()

        val manager = AdManagerUtil(AdManager.instance!!)

        val adRequest = AdRequest.build("someDisplayID");
        assertEquals(null, adRequest.type)
        assertEquals(true, adRequest.cachedAds.isNotEmpty())
        assertEquals("someDisplayID", adRequest.displayId)
    }
}
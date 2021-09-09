package engineer.trustmeimansoftware.interactionrewardingads.testlib


import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch

/**
 * @class AdNetworkManagerTest
 *
 */
class AdNetworkManagerTest {
    @Test(timeout = 2500)
    fun sendImpressionStats_triggers_callback() {

        val manager = TestAdNetworkManager()

        var called = false
        val latch = CountDownLatch(1)

        val cb: (String) -> Unit = {
            called = true
        }
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        val stats = ImpressionStats()
        scope.launch {
            manager.sendImpressionStats(null, stats, cb)
            latch.countDown()
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        assertEquals(true, called)
    }

    @Test(timeout = 2500)
    fun requestAd() {
        val manager = TestAdNetworkManager()
        manager.ad = InteractionRewardedAd("myID", "")

        val latch = CountDownLatch(1)
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        val request = AdRequest("publisherID", "appId", "displayID", arrayOf())
        scope.launch {
            val result = manager.requestAd(request)
            assertEquals("appId", result.appId)
            assertEquals("displayID", result.displayBlockId)
            latch.countDown()
        }
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.database.impression.ImpressionDatabase
import engineer.trustmeimansoftware.adlib.database.impression.ImpressionDbItem
import engineer.trustmeimansoftware.adlib.database.impression.getDatabase
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.Setup
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.suspendCoroutine

@RunWith(AndroidJUnit4::class)
class ImpressionDatabaseTest {

    @Test
    fun initializeDatabase() {
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val db = AdManager.instance!!.impressionDatabase
            assertEquals(true, db is ImpressionDatabase)
        }
    }

    @Test
    fun insertEntry() {
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val latch = CountDownLatch(1);
            val db = AdManager.instance!!.impressionDatabase!!

            val scope = CoroutineScope(Dispatchers.IO)

            var entry: ImpressionDbItem? = null

            // launch in non MainThread because that's forbidden
            scope.launch {
                db.impressionDao.deleteAll();
                db.impressionDao.insert(ImpressionDbItem("myId", "someData"));
                entry = db.impressionDao.getByImpressionId("myId");
                latch.countDown()
            }

            try {
                latch.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            assertEquals("myId", entry?.impressionId)
            assertEquals("someData", entry?.data)

        }
    }

    @Test
    fun getUnsentEntries() {
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val latch = CountDownLatch(1);
            val db = AdManager.instance!!.impressionDatabase!!

            val job = Job()
            val scope = CoroutineScope(job + Dispatchers.IO)

            var entries: List<ImpressionDbItem> = arrayOf<ImpressionDbItem>().toList()

            // launch in non MainThread because that's forbidden
            scope.launch {
                db.impressionDao.deleteAll();
                db.impressionDao.insert(ImpressionDbItem("myId", "someData"));
                db.impressionDao.insert(ImpressionDbItem("myId2", "someData", true));
                entries = db.impressionDao.getUnsent();
                latch.countDown()
            }

            try {
                latch.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            assertEquals(1, entries.size)
            assertEquals(false, entries.get(0).isSent)

        }
    }


    @Test(timeout = 1000)
    fun cacheEntryWhenSendingFailed() {
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        val latch = CountDownLatch(1);
        scenario.onActivity { activity ->

            val db = AdManager.instance!!.impressionDatabase!!

            val ad = Ad("adID", "somePathThatDoesntMatter")
            ad.requestResult = AdRequestResult("someImpressionId", "someAppId", "someDisplayBlockId",
                "someCampaignId", arrayOf(), false)

            val stats = ImpressionStats(true, 30, 30, 10, arrayOf(), arrayOf(), 60)

            val job = Job()
            val scope = CoroutineScope(job + Dispatchers.IO)

            var entries: List<ImpressionDbItem> = arrayOf<ImpressionDbItem>().toList()

            AdManager.instance!!.config.baseUrl = "malFormedUrl://blaaaaa"

            scope.launch {
                db.impressionDao.deleteAll();
                AdManager.instance!!.networkManager?.sendImpressionStats(ad, stats) {
                    entries = db.impressionDao.getUnsent()
                    assertEquals(1, entries.size)
                    latch.countDown()
                }
            }

        }
        latch.await()
    }
}
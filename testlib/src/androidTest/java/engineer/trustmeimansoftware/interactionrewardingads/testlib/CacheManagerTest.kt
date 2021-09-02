package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@RunWith(AndroidJUnit4::class)
class CacheManagerTest {
    @Test
    fun offline_getCachedAds() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AdManager.build(null);
        AdManager.instance?.context = appContext
        AdManager.instance?.networkManager = OfflineAdNetworkManager()
        AdManager.instance?.cacheManager = OfflineCacheManager()

        val manager = AdManagerUtil(AdManager.instance!!)

        val cachedAds = manager.cache.getCachedAdIDs();
        assertEquals(true, cachedAds.size > 0)
    }

    @Test
    fun getCachedAdIDs() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AdManager.build(null);
        AdManager.instance?.context = appContext
        AdManager.instance?.cacheManager = CacheManager()
        val manager = AdManagerUtil(AdManager.instance!!)
        val cache = manager.cache as CacheManager

        val cachedAdIds = cache.getCachedAdIDs()
        assertEquals(1, cachedAdIds.size)
        assertEquals("someAdID", cachedAdIds[0].id)
    }

    @Test
    fun existsAdForAdID() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AdManager.build(null);
        AdManager.instance?.context = appContext
        AdManager.instance?.cacheManager = CacheManager()
        val manager = AdManagerUtil(AdManager.instance!!)
        val cache = manager.cache as CacheManager

        val exists = cache.existsAdForActivity(null, "someAdID")
        assertEquals(true, exists)
    }

    @Before
    fun insertFilesInCacheDir() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // create folder for ads
        val adsDir = File(appContext.cacheDir, "ads")
        if(!adsDir.exists()) {
            adsDir.mkdir();
        }
        val myAdDir = File(adsDir, "someAdID")
        if(!myAdDir.exists()) {
            myAdDir.mkdir();
        }
        // create three empty files in ads dir
        val fHtml = File(myAdDir, "index.html")
        val fJs = File(myAdDir, "app.js")
        val fCss = File(myAdDir, "styles.css")

        val files = arrayOf(fHtml, fJs, fCss);
        for(f in files) {
            val writer = OutputStreamWriter(FileOutputStream(f))
            writer.write("testStr");
            writer.flush();
            writer.close();
        }
    }

    @After
    fun cleanupFilesInCacheDir() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val adsDir = File(appContext.cacheDir, "ads")
        if(adsDir.exists()) {
            adsDir.deleteRecursively();
        }
    }
}
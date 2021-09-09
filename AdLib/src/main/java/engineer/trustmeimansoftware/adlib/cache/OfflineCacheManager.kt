package engineer.trustmeimansoftware.adlib.cache

import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.manager.AdManager
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * CacheManager for Offline usage
 * <p>Links to the assets directory</p>
 * <p>if you want to include your own creatives in offline mode, put them into the assets/ads directory</p>
 */
class OfflineCacheManager(private val assetPath: String = "file:///android_asset") : ICacheManager {

    /**
     * returns the creatives path
     */
    override fun getFullCreativePath(adID: String): String {
        return "${assetPath}/ads/$adID/index.html"
    }

    /**
     * returns the creatives path
     */
    override fun getCreativePath(adID: String): String {
        return "ads/$adID/index.html"
    }

    /**
     * not needed in offline mode
     */
    override fun generateFilePath(adID: String, filename: String): String {
        throw Error("Stub")
    }

    /**
     * returns an Array of cached adIDs
     */
    override fun getCachedAdIDs(): Array<CachedAd> {
        val cachedAds = ArrayList<CachedAd>()
        val files = AdManager.instance!!.context!!.assets!!.list("ads/")
        //files = files?.filter { s -> !s.startsWith(".") }?.toTypedArray()
        files?.let {
            for (file: String in it) {
                cachedAds.add(CachedAd(file))
            }
        }
        return cachedAds.toTypedArray()
    }

    /**
     *
     * @param activity - the activity this called from
     * needed to access the resources
     *
     * @param adID the id of the ad
     *
     * @return Boolean - true if file exists
     */
    override fun existsAdForActivity(activity: AppCompatActivity?, adID: String): Boolean {
        var inputStream: InputStream? = null
        return try {
            inputStream = activity!!.resources.assets.open(getCreativePath(adID))
            true
            //File exists so do something with it
        } catch (ex: IOException) {
            false
        } finally {
            inputStream?.close()
        }
    }

    /**
     * not needed in offline mode
     */
    override fun prepareForDownload(adID: String): File {
        throw Error("Stub")
    }

    /**
     * not needed in offline mode
     */
    override fun deleteAdDirectory(adID: String) {
        throw Error("Stub")
    }

    /**
     * not needed in offline mode
     */
    override fun createTimestampForCreative(adID: String, timestamp: String) {
        throw Error("Stub")
    }
}
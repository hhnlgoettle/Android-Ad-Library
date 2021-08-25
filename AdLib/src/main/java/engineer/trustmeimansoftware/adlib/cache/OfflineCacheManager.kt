package engineer.trustmeimansoftware.adlib.cache

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.AdManager
import java.io.File
import java.io.IOException
import java.io.InputStream

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

    override fun generateFilePath(adID: String, filename: String): String {
        TODO("Not yet implemented")
    }

    /**
     * returns an Array of cached adIDs
     */
    override fun getCachedAdIDs(): Array<String> {
        val cachedAds = ArrayList<String>()
        var files = AdManager.instance!!.context!!.assets!!.list("ads/")
        //files = files?.filter { s -> !s.startsWith(".") }?.toTypedArray()
        Log.d("files", files?.size.toString())
        files?.let {
            for (file: String in it) {
                Log.d("file", file)
                cachedAds.add(file)
            }
        }
        return cachedAds.toTypedArray();
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

    override fun prepareForDownload(adID: String): File {
        throw Error("Stub")
    }

    override fun deleteAdDirectory(adID: String) {
        throw Error("Stub")
    }
}
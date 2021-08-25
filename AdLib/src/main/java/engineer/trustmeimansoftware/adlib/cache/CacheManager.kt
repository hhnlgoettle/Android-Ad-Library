package engineer.trustmeimansoftware.adlib.cache

import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.AdManager
import java.io.File
import java.io.IOException

class CacheManager() : ICacheManager {
    fun cachePath(): String {
        return AdManager.instance!!.context!!.cacheDir.absolutePath;
    }

    fun cacheDir(): File {
        return AdManager.instance!!.context!!.cacheDir
    }

    fun adDir(): File {
        return File(cacheDir(), "/ads")
    }

    fun adDir(adID: String): File {
        return File(adDir(), "/$adID")
    }


    /**
     * returns the creatives path
     */
    override fun getFullCreativePath(adID: String): String {
        return "file://${cachePath()}/ads/$adID/index.html"
    }

    /**
     * returns the creatives path
     */
    override fun getCreativePath(adID: String): String {
        return "/ads/$adID/index.html"
    }

    override fun generateFilePath(adID: String, filename: String): String {
        return cachePath()+"/ads/"+adID+"/"+filename
    }

    /**
     * returns an Array of cached adIDs
     */
    override fun getCachedAdIDs(): Array<String> {
        val cachedAds = ArrayList<String>()
        var files = File(cachePath()+"/ads").list()
        files?.let {
            for (file: String in it) {
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
        return try {
            val file = File(cachePath()+ "/ads/"+adID, "index.html")
            return file.exists()
        } catch (ex: IOException) {
            false
        }
    }

    override fun prepareForDownload(adID: String): File {
        var adDir = File(cachePath()+"/ads/"+adID)
        if(adDir.exists()) {
            deleteAdDirectory(adID);
        }
        adDir.mkdirs()
        return adDir
    }

    override fun deleteAdDirectory(adID: String) {
        var dir = File(cachePath()+"/ads/"+adID)
        if(!dir.exists()) {
            return;
        }
        dir.deleteRecursively();
    }
}
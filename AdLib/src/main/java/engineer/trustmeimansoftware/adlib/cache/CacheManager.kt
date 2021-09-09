package engineer.trustmeimansoftware.adlib.cache

import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.manager.AdManager
import java.io.*
import java.lang.Exception

/**
 * abstraction layer between file system
 */
class CacheManager() : ICacheManager {
    /**
     * @return the path to the cache directory
     */
    private fun cachePath(): String {
        return AdManager.instance!!.context!!.cacheDir.absolutePath
    }

    /**
     * @return the file that links to cache dir
     */
    private fun cacheDir(): File {
        return AdManager.instance!!.context!!.cacheDir
    }

    /**
     * @return the ads directory path
     */
    private fun adDir(): File {
        return File(cacheDir(), "/ads")
    }

    /**
     * @return file that links to adDirectory
     */
    fun adDir(adID: String): File {
        return File(adDir(), "/$adID")
    }


    /**
     * returns the creative's path with file:// prefix
     */
    override fun getFullCreativePath(adID: String): String {
        return "file://${cachePath()}/ads/$adID/index.html"
    }

    /**
     * returns the creative's path
     */
    override fun getCreativePath(adID: String): String {
        return "/ads/$adID/index.html"
    }

    /**
     * generates a filepath for a file to download
     */
    override fun generateFilePath(adID: String, filename: String): String {
        return cachePath()+"/ads/"+adID+"/"+filename
    }

    /**
     * returns an Array of cached adIDs
     */
    override fun getCachedAdIDs(): Array<CachedAd> {
        val cachedAds = ArrayList<CachedAd>()
        val files = File(cachePath()+"/ads").list()
        files?.let {
            for (file: String in it) {
                val timestamp = readTimestampForCreative(file)
                cachedAds.add(CachedAd(file, timestamp))
            }
        }
        return cachedAds.toTypedArray()
    }

    /**
     *
     * @param activity - the activity this called from
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

    /**
     * prepares a directory for download
     * deletes existing files
     * creates dir if not exists
     */
    override fun prepareForDownload(adID: String): File {
        val adDir = File(cachePath()+"/ads/"+adID)
        if(adDir.exists()) {
            deleteAdDirectory(adID)
        }
        adDir.mkdirs()
        return adDir
    }

    /**
     * deletes an ad directory
     */
    override fun deleteAdDirectory(adID: String) {
        val dir = File(cachePath()+"/ads/"+adID)
        if(!dir.exists()) {
            return
        }
        dir.deleteRecursively()
    }

    /**
     * creates a file called timestamp and stores the creatives timestamp there
     */
    override fun createTimestampForCreative(adID: String, timestamp: String) {
        val dir = File(cachePath()+"/ads/"+adID)
        if(!dir.exists()) {
            throw Exception("directory does not exist")
        }
        val file = File(dir, "timestamp")
        val writer = OutputStreamWriter(FileOutputStream(file))
        writer.write(timestamp)
        writer.flush()
        writer.close()
    }

    /**
     * reads a creatives' timestamp file and returns its content
     */
    private fun readTimestampForCreative(adID: String): String {
        var reader: InputStreamReader? = null
        try {
            val dir = File(cachePath() + "/ads/" + adID)
            if (!dir.exists()) {
                throw Exception("directory does not exist")
            }
            val file = File(dir, "timestamp")
            if(file.exists()) {
                reader = InputStreamReader(FileInputStream(file))
                return reader.readText()
            }
            return ""
        } finally {
            reader?.close()
        }
    }
}
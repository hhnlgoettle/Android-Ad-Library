package engineer.trustmeimansoftware.adlib.cache

import androidx.appcompat.app.AppCompatActivity
import java.io.File

interface ICacheManager {

    /**
     * returns the creatives path
     */
    fun getFullCreativePath(adID: String): String

    /**
     * returns the creatives path
     */
    fun getCreativePath(adID: String): String

    /**
     * generates the filepath for a given adID and filename to store the file
     */
    fun generateFilePath(adID: String, filename: String): String

    /**
     * returns an Array of cached adIDs
     */
    fun getCachedAdIDs(): Array<String>

    /**
     *
     * @param activity - the activity this called from
     * needed to access the resources
     *
     * @param adID the id of the ad
     *
     * @return Boolean - true if file exists
     */
    fun existsAdForActivity(activity: AppCompatActivity?, adID: String): Boolean

    /**
     * prepares a directory for download
     * deletes contents if any exist
     */
    fun prepareForDownload(adID: String): File

    /**
     * deletes the directory of a given ad
     */
    fun deleteAdDirectory(adID: String)
}
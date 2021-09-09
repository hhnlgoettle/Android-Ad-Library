package engineer.trustmeimansoftware.adlib.ad

import engineer.trustmeimansoftware.adlib.jsonutil.JSONUtil
import org.json.JSONObject

/**
 * models a result from an AdRequest answered by the AdWebserver
 */
data class AdRequestResult(
    /**
     * unique id of this impression
     */
    val impressionId: String,
    val appId: String,
    val displayBlockId: String,
    val campaignId: String,
    /**
     * urls of files to download from the AdWebserver
     */
    val downloadUrls: Array<DownloadUrlItem>,
    val cached: Boolean = false,
    val creativeTimestamp: String = ""
) {
    companion object {
        /**
         * generates an AdRequestResult from a Json String
         */
        fun fromJSONString(json: String): AdRequestResult {
            val jsonObj = JSONObject(json)
            val appId = JSONUtil.readNullableString(jsonObj, "appId", "")
            val impressionId = JSONUtil.readNullableString(jsonObj, "impressionId", "")
            val displayBlockId = JSONUtil.readNullableString(jsonObj, "displayBlockId", "")
            val campaignId = JSONUtil.readNullableString(jsonObj, "campaignId", "")
            val cached = JSONUtil.readBoolean(jsonObj, "cached", false)
            val creativeTimestamp = JSONUtil.readNullableString(jsonObj, "creativeTimestamp", "")

            var downloadUrls = ArrayList<DownloadUrlItem>()
            val downloadUrlsAsJSONArray = JSONUtil.readNullableJSONArray(jsonObj, "downloadUrls")
            downloadUrlsAsJSONArray?.let {
                for(i in 0 until downloadUrlsAsJSONArray.length()) {
                    val item = downloadUrlsAsJSONArray[i] as JSONObject
                    val url = JSONUtil.readNullableString(item, "url", "")
                    val filename = JSONUtil.readNullableString(item, "filename", "")
                    downloadUrls.add(DownloadUrlItem(url!!, filename!!))
                }

            }
            return AdRequestResult(impressionId!!, appId!!, displayBlockId!!, campaignId!!, downloadUrls.toTypedArray(), cached, creativeTimestamp!!)
        }
    }
}

/**
 * models a DownloadUrlItem with url and filename
 */
data class DownloadUrlItem(val url: String, val filename: String)

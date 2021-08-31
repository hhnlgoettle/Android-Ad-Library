package engineer.trustmeimansoftware.adlib.ad

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.cache.CachedAd

/**
 * models an Request to the AdWebserver
 * @param publisherId the publisher's id
 * @param appId the app's appId at the AdWebserver
 * @param displayId the display's id at the AdWebserver
 * @param cachedAds cached ads by the library
 * @param type type of ad requested
 *
 */
class AdRequest(val publisherId: String, val appId: String, val displayId: String, val cachedAds: Array<CachedAd>, var type: String? = null) {

    companion object {
        /**
         * @param displayID - the ID of the displayBlock in which the Ad will be shown
         * do not use the same displayID for different displayBlocks. The ID is used to generate exact metrics by displayBlock.
         */
        fun build(displayID: String): AdRequest {
            var cachedAds = arrayOf<CachedAd>();
            AdManager.instance?.cacheManager?.getCachedAdIDs()?.let {
                cachedAds = it
            }
            return AdRequest("", AdManager.instance!!.appId!!, displayID, cachedAds)
        }
    }
}
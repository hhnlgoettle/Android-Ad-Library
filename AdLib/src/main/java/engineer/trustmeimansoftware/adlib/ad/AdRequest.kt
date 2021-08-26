package engineer.trustmeimansoftware.adlib.ad

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.cache.CachedAd

/**
 * @class AdRequest
 *
 * used to request an ad from the AdWebServer
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
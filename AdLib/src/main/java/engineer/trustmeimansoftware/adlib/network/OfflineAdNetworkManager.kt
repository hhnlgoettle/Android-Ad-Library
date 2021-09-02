package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import java.io.File

/**
 * Stump connector when using library in offline mode
 */
class OfflineAdNetworkManager : IAdNetworkManager {
    /**
     * returns a cached Ad if one exists
     */
    override suspend fun getAd(adRequest: AdRequest): Ad {
        if(adRequest.type != InteractionRewardedAd::class.java.toString()) throw Error("unsupported type ${adRequest.type}, should be ${InteractionRewardedAd::class.java.toString()}")

        if(adRequest.cachedAds.isEmpty()) {
            throw Exception("no cached Ads available")
        }

        val adID = adRequest.cachedAds.random().id
        return InteractionRewardedAd(adID, AdManager.instance?.cacheManager?.getFullCreativePath(adID).toString());
    }

    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        throw Error("Stub")
    }

    override suspend fun downloadAd(url: String, file: File) {
        throw Error("Stub")
    }

    override suspend fun downloadUrlItems(downloadUrlItems: Array<DownloadUrlItem>, destination: String) {
        throw Error("Stub")
    }

    override fun sendImpressionStats(
        ad: Ad?,
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        throw Error("Stub")
    }
}
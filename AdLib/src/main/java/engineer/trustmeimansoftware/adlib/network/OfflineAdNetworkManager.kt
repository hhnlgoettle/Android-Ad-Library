package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

class OfflineAdNetworkManager : IAdNetworkManager {
    override suspend fun getAd(adRequest: AdRequest): Ad {
        if(adRequest.type != InteractionRewardedAd::class.java.toString()) throw Error("unsupported type ${adRequest.type}, should be ${InteractionRewardedAd::class.java.toString()}")

        if(adRequest.cachedAds.isEmpty()) {
            throw Exception("no cached Ads available")
        }

        val adID = adRequest.cachedAds.random()
        return InteractionRewardedAd(adID, AdManager.instance?.cacheManager?.getFullCreativePath(adID).toString());
    }

    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        TODO("Not yet implemented")
    }

    override suspend fun downloadAd(url: String, path: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendImpressionStats(
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        TODO("Not yet implemented")
    }
}
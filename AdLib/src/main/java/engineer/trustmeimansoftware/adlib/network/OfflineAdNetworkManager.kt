package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import java.io.File

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

    override suspend fun downloadAd(url: String, file: File) {
        TODO("Not yet implemented")
    }

    override suspend fun downloadUrlItems(downloadUrlItems: Array<DownloadUrlItem>, destination: String) {
        TODO("Not yet implemented")
    }

    override fun sendImpressionStats(
        ad: Ad?,
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        TODO("Not yet implemented")
    }
}
package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

class AdNetworkManager : IAdNetworkManager{

    override suspend fun getAd(adRequest: AdRequest): Ad {
        // TODO call server
        // TODO download urls
        // return ad
        when (adRequest.type) {
            Ad::class.java.toString() -> throw Error("Type ${adRequest.type} is not supported")
            FullscreenAd::class.java.toString() -> throw Error("Type ${adRequest.type} is not supported")
            InteractionRewardedAd::class.java.toString() -> return InteractionRewardedAd(
                "mockAdID",
                AdManager.instance?.cacheManager?.getFullCreativePath("mockAdID").toString())
            else -> throw Error("Type ${adRequest.type} is not supported")
        }
    }

    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        throw Error("Stump")
    }

    override suspend fun downloadAd(url: String, path: String) {
        throw Error("Stump")
    }

    override suspend fun sendImpressionStats(
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        throw Error("Stump")
    }

}
package engineer.trustmeimansoftware.interactionrewardingads.testlib

import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TestAdNetworkManager : IAdNetworkManager{

    var downloadUrls : Array<String> = arrayOf()
    var ad: InteractionRewardedAd? = null
    override suspend fun getAd(adRequest: AdRequest): Ad {
        return requestAd(adRequest).ad
    }


    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        val result = AdRequestResult(adRequest, ad!!.getID(), ad!!, arrayOf())
        withContext(Dispatchers.IO) {
            delay(5)
        }
        return result
    }

    override suspend fun downloadAd(url: String, path: String) {
        withContext(Dispatchers.IO) {
            delay(5)

        }
    }

    override suspend fun sendImpressionStats(
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        try {
            withContext(Dispatchers.IO) {
                cb?.let {
                    it("cb: #sendImpressionStats")
                }
            }
        } catch (e: InterruptedException) {
        }
    }
}
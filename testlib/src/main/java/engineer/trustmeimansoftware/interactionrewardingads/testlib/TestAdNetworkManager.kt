package engineer.trustmeimansoftware.interactionrewardingads.testlib

import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

class TestAdNetworkManager : IAdNetworkManager{

    var downloadUrls : Array<String> = arrayOf()
    var ad: InteractionRewardedAd? = null
    override suspend fun getAd(adRequest: AdRequest): Ad {
        return ad!!
    }


    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        val result = AdRequestResult(
            "impressionId",
            adRequest.appId,
            adRequest.displayId,
            "campaignId",
            arrayOf(),
            false)
        withContext(Dispatchers.IO) {
            delay(5)
        }
        return result
    }

    override suspend fun downloadAd(url: String, file: File) {
        withContext(Dispatchers.IO) {
            delay(5)

        }
    }

    override suspend fun downloadUrlItems(
        downloadUrlItems: Array<DownloadUrlItem>,
        destination: String
    ) {
        TODO("Not yet implemented")
    }

    override fun sendImpressionStats(
        ad: Ad?,
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        cb?.let {
            it("cb: #sendImpressionStats")
        }
    }
}
package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.net.URL


class AdNetworkManager : IAdNetworkManager{

    /**
     * returns an Ad
     */
    override suspend fun getAd(adRequest: AdRequest): Ad {
        // filter unsupported types
        when (adRequest.type) {
            Ad::class.java.toString() -> throw Error("Type ${adRequest.type} is not supported")
            FullscreenAd::class.java.toString() -> throw Error("Type ${adRequest.type} is not supported")
            InteractionRewardedAd::class.java.toString() -> {}
            else -> throw Error("Type ${adRequest.type} is not supported")
        }

        // request ad from server
        val adRequestResult = requestAd(adRequest)

        // if ad is not cached, download it
        if(!adRequestResult.cached) {
            downloadUrlItems(adRequestResult.downloadUrls, adRequestResult.campaignId)
            AdManager.instance?.cacheManager?.createTimestampForCreative(adRequestResult.campaignId, adRequestResult.creativeTimestamp)
        }

        val ad =  InteractionRewardedAd(
            adRequestResult.campaignId,
            AdManager.instance?.cacheManager?.getFullCreativePath(adRequestResult.campaignId).toString())
        ad.requestResult = adRequestResult

        return ad
    }

    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        return AdRequestApi.loadAdRequest(adRequest)
    }

    override suspend fun downloadAd(url: String, file: File) {
        URL(url).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
                output.close()
            }
            input.close()
        }
    }

    override suspend fun downloadUrlItems(downloadUrlItems: Array<DownloadUrlItem>, adID: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        val cacheManager = AdManager.instance?.cacheManager!! as CacheManager
        val baseUrl = AdManager.instance!!.baseUrl
        val adDir = cacheManager.prepareForDownload(adID)

        // download all files in parallel
        val promises = ArrayList<Deferred<Unit>>()
        try {
            for(item in downloadUrlItems) {
                val job = scope.async {
                    val file = File(adDir, item.filename)
                    downloadAd(baseUrl+item.url, file)
                }
                promises.add(job)
            }
            for(item in promises) {
                item.await()
            }
            cacheManager.createTimestampForCreative(adID, )
        } catch (err: Throwable) {
            // if error occurs, delete the ad content
            cacheManager.deleteAdDirectory(adID)
            throw err
        }

    }

    override fun sendImpressionStats(
        ad: Ad?,
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        try {
            // TODO on network failure cache impression stats
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    AdImpressionApi.sendAdImpression(ad!!, impressionStats)
                } catch (ex: Exception) {
                    println("error while sending impression stats: "+ex.message)
                }
            }
        } catch(throwable: Throwable) {
            println("error while sending impression stats: "+throwable.message)
        }
    }

}
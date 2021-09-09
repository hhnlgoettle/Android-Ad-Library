package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.*
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.database.impression.ImpressionDbItem
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.net.URL
import java.util.concurrent.CountDownLatch


/**
 * NetworkManager that handles loading ads from the network
 */
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

        // if result has no campaignId, this indicates server has found no matching ad
        if(adRequestResult.campaignId.isEmpty() || adRequestResult.campaignId == "") {
            throw Error("no matching ad found")
        }

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

    /**
     * calls Api with an AdRequest
     */
    override suspend fun requestAd(adRequest: AdRequest): AdRequestResult {
        return AdRequestApi.loadAdRequest(adRequest)
    }

    /**
     * downloads an ad item
     */
    override suspend fun downloadAd(url: String, file: File) {
        URL(url).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
                output.close()
            }
            input.close()
        }
    }

    /**
     * downloads all ad creative items
     */
    override suspend fun downloadUrlItems(downloadUrlItems: Array<DownloadUrlItem>, adID: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        val cacheManager = AdManager.instance?.cacheManager!! as CacheManager
        val baseUrl = AdManager.instance!!.config.baseUrl
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

    /**
     * sends impression stats to the webserver
     */
    override fun sendImpressionStats(
        ad: Ad?,
        impressionStats: ImpressionStats,
        cb: ((String) -> Unit)?
    ) {
        try {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    AdImpressionApi
                        .sendAdImpression(ad!!, impressionStats)
                } catch (ex: Throwable) {
                    // cache impression when request failed
                    AdManager.instance?.impressionDatabase?.let {
                        it.impressionDao.insert(
                            ImpressionDbItem(
                                ad?.requestResult!!.impressionId,
                                ImpressionStats.toJSONString(impressionStats),
                                false
                            )
                        )
                    }
                }
                cb?.invoke("done")
            }
        } catch(throwable: Throwable) {
            println("caught throwable: "+throwable.message)
        }
    }

}
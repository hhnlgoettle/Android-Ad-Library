package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.ad.DownloadUrlItem
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import java.io.File

/**
 * @interface IAdNetworkManager
 *
 * Manage Network connections
 */
interface IAdNetworkManager {
    suspend fun getAd(adRequest: AdRequest): Ad

    suspend fun requestAd(adRequest: AdRequest): AdRequestResult

    suspend fun downloadAd(url: String, file: File)

    suspend fun downloadUrlItems(downloadUrlItems: Array<DownloadUrlItem>, adID: String)

    fun sendImpressionStats(ad: Ad?, impressionStats: ImpressionStats, cb: ((String) -> Unit)? = null)
}
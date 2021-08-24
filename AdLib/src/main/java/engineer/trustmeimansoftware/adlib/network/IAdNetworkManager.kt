package engineer.trustmeimansoftware.adlib.network

import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

/**
 * @interface IAdNetworkManager
 *
 * Manage Network connections
 */
interface IAdNetworkManager {
    suspend fun getAd(adRequest: AdRequest): Ad

    suspend fun requestAd(adRequest: AdRequest): AdRequestResult

    suspend fun downloadAd(url: String, path: String)

    suspend fun sendImpressionStats(impressionStats: ImpressionStats, cb: ((String) -> Unit)? = null)
}
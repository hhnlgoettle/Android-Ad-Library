package engineer.trustmeimansoftware.adlib.callback

import android.util.Log
import engineer.trustmeimansoftware.adlib.ad.Ad
import java.lang.Error

/**
 * @interface AdLoadCallback
 *
 * used for Ads. Called when an Ad is loaded from the AdWebServer
 */
interface AdLoadCallback {
    fun onAdLoaded(ad: Ad)

    fun onAdFailedToLoad(error: Error)
}
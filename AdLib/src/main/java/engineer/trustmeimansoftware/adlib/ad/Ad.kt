package engineer.trustmeimansoftware.adlib.ad

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import kotlinx.coroutines.*

/**
 * @class Ad
 *
 * a basic ad
 *
 * @param id - the unique id of the ad
 * @param path - where to find the ad
 */
open class Ad(private val id: String, var path: String) {

    fun getID(): String {
        return id
    }

    companion object {
        /**
         * @param adRequest the AdRequest used to request this ad from the AdWebServer
         * @param adLoadCallback the callback called when this ad is downloaded and ready
         */
        fun load(activity: AppCompatActivity, adRequest: AdRequest, adLoadCallback: AdLoadCallback) {
            if(adRequest.type == null) adRequest.type = this::class.java.declaringClass.toString()
            // launch the coroutine in activity's lifecycle scope
            // this will cancel the job when the lifecycle owner aka the activity stops
            activity.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                        // TODO load ad with AdNetworkManager
                    try {
                        val ad = AdManager.instance?.networkManager?.getAd(adRequest)
                        if (ad != null) {
                            activity.runOnUiThread {
                                adLoadCallback.onAdLoaded(ad)
                            }
                        } else {
                            activity.runOnUiThread {
                                adLoadCallback.onAdFailedToLoad(Error("Ad is null"))
                            }
                        }
                    } catch (e: Exception) {
                        activity.runOnUiThread {
                            adLoadCallback.onAdFailedToLoad(Error("Ad is null"))
                        }
                    }
                    return@withContext
                }
            }
        }

    }
}
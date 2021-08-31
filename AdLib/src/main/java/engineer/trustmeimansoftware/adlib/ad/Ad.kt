package engineer.trustmeimansoftware.adlib.ad

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import kotlinx.coroutines.*

/**
 * models a basic ad
 *
 * @param id - the unique id of the ad
 * @param path - where to find the ad
 * @param requestResult the result of the [AdRequest] that led to this ad
 */
open class Ad(private val id: String, var path: String, var requestResult: AdRequestResult? = null) {

    fun getID(): String {
        return id
    }

    companion object {
        /**
         * loads an Ad in the activities lifecycleScope
         *
         * @param adRequest the AdRequest used to request this ad from the AdWebServer
         * @param adLoadCallback the callback called when this ad is downloaded and ready
         */
        fun load(activity: AppCompatActivity, adRequest: AdRequest, adLoadCallback: AdLoadCallback) {
            if(adRequest.type == null) adRequest.type = this::class.java.declaringClass.toString()
            // launch the coroutine in activity's lifecycle scope
            // this will cancel the job when the lifecycle owner aka the activity stops
            activity.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
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
                            adLoadCallback.onAdFailedToLoad(Error("Ad is null. Reason: "+e.message))
                        }
                    }
                    return@withContext
                }
            }
        }

    }
}
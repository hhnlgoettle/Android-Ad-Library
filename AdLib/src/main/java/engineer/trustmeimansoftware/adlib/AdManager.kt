package engineer.trustmeimansoftware.adlib

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.cache.ICacheManager
import engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.jsinterface.JavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.network.AdNetworkManager
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.adlib.registry.AdRegistry
import engineer.trustmeimansoftware.adlib.util.AppId

/**
 * @class AdManager
 *
 * holds refs to all relevant Ad related classes
 */
class AdManager: IAdManager {
    override var baseUrl: String = "https://ads.trustmeimansoftware.engineer"
    override var context: Context? = null

    override var adFullscreenActivityBuilder: IAdFullscreenActivityBuilder? = null
    override var adRegistry: AdRegistry? = null
    override var cacheManager: ICacheManager? = null
    override var jsInterfaceBuilder: IJavaScriptInterfaceBuilder? = null
    override var networkManager: IAdNetworkManager? = null

    override var appId: String? = null
    /**
     * initialize()
     */
    override fun initialize(activity: AppCompatActivity?) {
        initialize(activity, AdManagerBuildOpts.default())
    }

    override fun initialize(activity: AppCompatActivity?, buildOpts: AdManagerBuildOpts) {
        if (activity != null) {
            context = activity.applicationContext
            appId = AppId.getAppIdFromContext(context!!)
        }
        activity?.let {
            adFullscreenActivityBuilder = AdFullscreenActivityBuilder(activity)
        }
        adRegistry = AdRegistry()
        jsInterfaceBuilder = JavaScriptInterfaceBuilder()
        networkManager = if(buildOpts.offlineMode) OfflineAdNetworkManager() else AdNetworkManager()
        cacheManager = if(buildOpts.offlineMode) OfflineCacheManager() else CacheManager()
    }


    companion object {
        var instance: IAdManager? = null

        /**
         * build
         *
         * populate the instance and initialize it
         */
        fun build(activity: AppCompatActivity?, buildOpts: AdManagerBuildOpts? = null) {
            if(instance == null) {
                instance = AdManager()
            }
            if(buildOpts == null) {
                instance!!.initialize(activity)
            } else {
                instance!!.initialize(activity, buildOpts)
            }

        }
    }

}
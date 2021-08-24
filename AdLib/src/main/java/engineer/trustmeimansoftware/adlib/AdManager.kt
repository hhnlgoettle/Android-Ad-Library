package engineer.trustmeimansoftware.adlib

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.jsinterface.JavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.network.AdNetworkManager
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager
import engineer.trustmeimansoftware.adlib.registry.AdRegistry

/**
 * @class AdManager
 *
 * holds refs to all relevant Ad related classes
 */
class AdManager: IAdManager {
    override var context: Context? = null

    override var adFullscreenActivityBuilder: IAdFullscreenActivityBuilder? = null
    override var adRegistry: AdRegistry? = null
    override var cacheManager: CacheManager? = null
    override var jsInterfaceBuilder: IJavaScriptInterfaceBuilder? = null
    override var networkManager: IAdNetworkManager? = null

    /**
     * initialize()
     */
    override fun initialize(activity: AppCompatActivity?) {
        initialize(activity, AdManagerBuildOpts.default())
    }

    override fun initialize(activity: AppCompatActivity?, buildOpts: AdManagerBuildOpts) {
        if (activity != null) {
            context = activity.applicationContext
        }
        activity?.let {
            adFullscreenActivityBuilder = AdFullscreenActivityBuilder(activity)
        }
        adRegistry = AdRegistry()
        cacheManager = CacheManager()
        jsInterfaceBuilder = JavaScriptInterfaceBuilder()
        networkManager = if(buildOpts.offlineMode) OfflineAdNetworkManager() else AdNetworkManager()
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
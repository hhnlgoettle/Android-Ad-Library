package engineer.trustmeimansoftware.adlib.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.adactivity.AdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivityBuilder
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
 * initializes relevant objects and stores references to them
 */
class AdManager: IAdManager {
    /**
     * Activity Context
     */
    override var context: Context? = null

    /**
     * used to launch AdFullscreenActivity
     */
    override var adFullscreenActivityBuilder: IAdFullscreenActivityBuilder? = null

    /**
     * holds refs to ads
     */
    override var adRegistry: AdRegistry? = null

    /**
     * manages the cache in which creatives are stored
     */
    override var cacheManager: ICacheManager? = null

    /**
     * builds the JavaScriptInterface attached to the webview when displaying the creative
     */
    override var jsInterfaceBuilder: IJavaScriptInterfaceBuilder? = null

    /**
     * handles network request
     */
    override var networkManager: IAdNetworkManager? = null


    override var config: AdManagerConfig = AdManagerConfig.getDefault()


    /**
     * the publisher's appId
     */
    override var appId: String? = null

    /**
     * initializes with default build options
     */
    override fun initialize(activity: AppCompatActivity?) {
        initialize(activity, AdManagerBuildOpts.default())
    }

    /**
     * initializes instance
     * @param activity the activity
     * @param buildOpts used to customize AdManager for e.g. offline use
     */
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
        if(buildOpts.testMode) config = AdManagerConfig.getTestConfig()
    }


    companion object {
        /**
         * the instance
         */
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
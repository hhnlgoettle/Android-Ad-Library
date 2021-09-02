package engineer.trustmeimansoftware.adlib.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.cache.ICacheManager
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.registry.AdRegistry

/**
 * interface for AdManagers
 */
interface IAdManager {
    var appId: String?
    var context: Context?
    var adFullscreenActivityBuilder: IAdFullscreenActivityBuilder?
    var adRegistry: AdRegistry?
    var cacheManager: ICacheManager?
    var jsInterfaceBuilder: IJavaScriptInterfaceBuilder?
    var networkManager: IAdNetworkManager?
    var config: AdManagerConfig


    fun initialize(activity: AppCompatActivity?)
    fun initialize(activity: AppCompatActivity?, buildOpts: AdManagerBuildOpts)
}
package engineer.trustmeimansoftware.adlib

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.registry.AdRegistry

interface IAdManager {
    var context: Context?
    var adFullscreenActivityBuilder: IAdFullscreenActivityBuilder?
    var adRegistry: AdRegistry?
    var cacheManager: CacheManager?
    var jsInterfaceBuilder: IJavaScriptInterfaceBuilder?
    var networkManager: IAdNetworkManager?

    fun initialize(activity: AppCompatActivity?)
    fun initialize(activity: AppCompatActivity?, buildOpts: AdManagerBuildOpts)
}
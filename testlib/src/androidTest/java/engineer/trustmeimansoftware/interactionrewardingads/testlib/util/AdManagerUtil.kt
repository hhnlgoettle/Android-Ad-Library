package engineer.trustmeimansoftware.interactionrewardingads.testlib.util

import engineer.trustmeimansoftware.adlib.IAdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.IAdManager
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.cache.ICacheManager
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder
import engineer.trustmeimansoftware.adlib.network.IAdNetworkManager
import engineer.trustmeimansoftware.adlib.registry.AdRegistry

class AdManagerUtil(private val adManager: IAdManager) {

    val screen: IAdFullscreenActivityBuilder
    get() = getAdFullscreenBuilder()

    val cache: ICacheManager
    get() = getCacheManager()

    val js: IJavaScriptInterfaceBuilder
    get() = getJSInterfaceBuilder()

    val registry: AdRegistry
    get() = getAdRegistry()

    val network: IAdNetworkManager
    get() = getNetworkManager()


    private fun getAdFullscreenBuilder(): IAdFullscreenActivityBuilder {
        return adManager.adFullscreenActivityBuilder!!
    }
    private fun getJSInterfaceBuilder(): IJavaScriptInterfaceBuilder {
        return adManager.jsInterfaceBuilder!!
    }
    private fun getAdRegistry(): AdRegistry {
        return adManager.adRegistry!!
    }
    private fun getCacheManager(): ICacheManager {
        return adManager.cacheManager!!
    }
    private fun getNetworkManager(): IAdNetworkManager {
        return adManager.networkManager!!
    }

}
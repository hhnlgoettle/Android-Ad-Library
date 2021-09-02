package engineer.trustmeimansoftware.adlib.manager

/**
 * build options for the adManager
 */
class AdManagerBuildOpts() {
    /**
     * if set to true, AdManager will use
     * <p> [engineer.trustmeimansoftware.adlib.network.OfflineAdNetworkManager] as networkManager
     * <p> [engineer.trustmeimansoftware.adlib.cache.OfflineCacheManager] as cacheManager
     */
    var offlineMode: Boolean = false
    var testMode: Boolean = false

    companion object {
        /**
         * returns an default config object
         */
        fun default(): AdManagerBuildOpts {
            val opts = AdManagerBuildOpts()
            opts.offlineMode = false
            return opts
        }
    }

}
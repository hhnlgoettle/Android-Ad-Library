package engineer.trustmeimansoftware.adlib

class AdManagerBuildOpts() {
    var offlineMode: Boolean = false

    companion object {
        fun default():AdManagerBuildOpts {
            val opts = AdManagerBuildOpts()
            opts.offlineMode = false
            return opts
        }
    }

}
package engineer.trustmeimansoftware.adlib

/**
 * models configuration for an [AdManager]
 */
class AdManagerConfig() {
    var showInfoTextPreAdDisplay: Boolean = true
    var infoTextDisplayTimeInMillis: Long = 3500

    companion object {
        /**
         * returns a default config object
         */
        fun getDefault(): AdManagerConfig {
            return AdManagerConfig()
        }

        /**
         * returns a test config with disabled [showInfoTextPreAdDisplay]
         */
        fun getTestConfig(): AdManagerConfig {
            val conf = AdManagerConfig()
            conf.showInfoTextPreAdDisplay = false
            conf.infoTextDisplayTimeInMillis = 10
            return conf
        }
    }
}

package engineer.trustmeimansoftware.adlib

import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

interface IAdFullscreenActivity {

    /**
     *
     */
    fun finishActivity(stats: ImpressionStats? = null)

    /**
     * closes the activity with an error
     * @param error - the cause
     */
    fun finishActivityWithError(error: java.lang.Error, stats: ImpressionStats? = null)
}
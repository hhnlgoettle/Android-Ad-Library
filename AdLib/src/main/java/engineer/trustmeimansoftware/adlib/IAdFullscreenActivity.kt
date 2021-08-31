package engineer.trustmeimansoftware.adlib

import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

/**
 * interface for the IAdFullscreenActivity
 */
interface IAdFullscreenActivity {

    /**
     * finishes the activity
     */
    fun finishActivity(stats: ImpressionStats? = null)

    /**
     * finishes the activity with an error
     * @param error - the cause
     */
    fun finishActivityWithError(error: java.lang.Error, stats: ImpressionStats? = null)
}
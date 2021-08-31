package engineer.trustmeimansoftware.adlib.callback

import java.lang.Error

/**
 * @interface FullscreenContentCallback
 *
 * Provides callbacks for displaying a FullscreenAd
 */
interface FullscreenContentCallback {
    /**
     * called when a FullscreenAd is dismissed, closed
     */
    fun onDismissed()

    /**
     *
     * called when a FullscreenAd cannot be displayed
     * @param error - the reason of failure
     */
    fun onFailedToShow(error: Error)
}
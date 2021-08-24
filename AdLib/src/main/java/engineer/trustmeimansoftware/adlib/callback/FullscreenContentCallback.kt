package engineer.trustmeimansoftware.adlib.callback

import java.lang.Error

/**
 * @interface FullscreenContentCallback
 *
 * Provides callbacks for displaying a FullscreenAd
 */
interface FullscreenContentCallback {
    /**
     * onDismissed
     *
     * called when a FullscreenAd is dismissed, closed
     */
    fun onDismissed()

    /**
     * onFailedToShow
     *
     * called when a FullscreenAd cannot be displayed
     * @param error - the reason of failure
     */
    fun onFailedToShow(error: Error)
}
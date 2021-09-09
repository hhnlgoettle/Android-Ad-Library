package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.webkit.JavascriptInterface
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivity
import engineer.trustmeimansoftware.adlib.jsinterface.AbstractJavaScriptInterface

/**
 * @class TestJavaScriptInterface
 *
 * used to test callbacks
 */
class TestJavaScriptInterface(activity: IAdFullscreenActivity) : AbstractJavaScriptInterface(activity) {

    private val TAG = "TestJavaScriptInterface"
    var onStartCb: (() -> Unit)? = null
    var onCloseCb: ((String) -> Unit)? = null
    var onCloseOnErrorCb: ((String, String) -> Unit)? = null

    @JavascriptInterface
    override fun onStart() {
        onStartCb?.let { it() }
    }

    @JavascriptInterface
    override fun onClose(jsonStats: String) {
        onCloseCb?.invoke(jsonStats)
    }

    @JavascriptInterface
    override fun onCloseOnError(errorMessage: String, jsonStats: String) {
        onCloseOnErrorCb?.invoke(errorMessage, jsonStats)
    }
}
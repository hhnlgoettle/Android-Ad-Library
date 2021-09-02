package engineer.trustmeimansoftware.adlib.jsinterface

import android.util.Log
import android.webkit.JavascriptInterface
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivity

/**
 * base class for JavaScriptInterface
 */
abstract class AbstractJavaScriptInterface(val activity: IAdFullscreenActivity) {
    var onStartFunc: (() -> Unit)? = null

    /**
     * called when init of JsLibrary is done in the webview
     */
    @JavascriptInterface
    open fun onStart() {
        Log.d(JavaScriptInterface.TAG, "onStart")
    }

    /**
     * called when Impression ended
     */
    @JavascriptInterface
    open fun onClose(jsonStats: String) {
        Log.d(JavaScriptInterface.TAG, "onClose")
    }

    /**
     * called when impression ended with an error
     */
    @JavascriptInterface
    open fun onCloseOnError(errorMessage: String, jsonStats: String) {
        Log.d(JavaScriptInterface.TAG, "onCloseOnError")
    }
}
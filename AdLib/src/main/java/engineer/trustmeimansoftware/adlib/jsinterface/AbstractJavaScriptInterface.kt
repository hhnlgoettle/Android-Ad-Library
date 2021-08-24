package engineer.trustmeimansoftware.adlib.jsinterface

import android.util.Log
import android.webkit.JavascriptInterface
import engineer.trustmeimansoftware.adlib.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.IAdFullscreenActivity

abstract class AbstractJavaScriptInterface(val activity: IAdFullscreenActivity) {
    @JavascriptInterface
    open fun onStart() {
        Log.d(JavaScriptInterface.TAG, "onStart")
    }

    @JavascriptInterface
    open fun onClose(jsonStats: String) {
        Log.d(JavaScriptInterface.TAG, "onClose")
    }

    @JavascriptInterface
    open fun onCloseOnError(errorMessage: String, jsonStats: String) {
        Log.d(JavaScriptInterface.TAG, "onCloseOnError")
    }
}
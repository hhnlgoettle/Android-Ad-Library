package engineer.trustmeimansoftware.adlib.jsinterface

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.IAdFullscreenActivity
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats

class JavaScriptInterface(activity: IAdFullscreenActivity) : AbstractJavaScriptInterface(activity) {

    companion object {
        const val TAG = "JavaScriptInterface"
    }

    @JavascriptInterface
    override fun onStart() {
        Log.d(TAG, "onStart")
        onStartFunc?.let {
            it.invoke()
        }
    }
    @JavascriptInterface
    override fun onClose(jsonStats: String) {
        val stats = ImpressionStats.fromJSON(jsonStats)
        (activity as AppCompatActivity).runOnUiThread() {
            activity.finishActivity(stats)
        }
    }
    @JavascriptInterface
    override fun onCloseOnError(errorMessage: String, jsonStats: String) {
        Log.d(TAG, "onCloseOnError")
        val stats = ImpressionStats.fromJSON(jsonStats)
        (activity as AppCompatActivity).runOnUiThread() {
            activity.finishActivityWithError(Error(errorMessage), stats)
        }
    }

}
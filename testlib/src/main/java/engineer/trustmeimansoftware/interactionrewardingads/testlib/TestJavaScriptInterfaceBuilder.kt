package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.annotation.SuppressLint
import android.util.Log
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivity
import engineer.trustmeimansoftware.adlib.jsinterface.AbstractJavaScriptInterface
import engineer.trustmeimansoftware.adlib.jsinterface.IJavaScriptInterfaceBuilder

class TestJavaScriptInterfaceBuilder : IJavaScriptInterfaceBuilder {

    var onStartCb: (() -> Unit)? = null
    var onCloseCb: ((String) -> Unit)? = null
    var onCloseOnErrorCb: ((String, String) -> Unit)? = null

    var instance: AbstractJavaScriptInterface? = null
    var onNewInstanceCb: ((AbstractJavaScriptInterface) -> Unit)? = null;

    @SuppressLint("LongLogTag")
    override fun build(activity: IAdFullscreenActivity): AbstractJavaScriptInterface {
        try {
            Log.d("TestJavaScriptInterfaceBuilder", "build new js Interface")
            val item = TestJavaScriptInterface(activity)
            item.onStartCb = onStartCb
            item.onCloseCb = onCloseCb
            item.onCloseOnErrorCb = onCloseOnErrorCb
            instance = item;
            return item
        } finally {
            onNewInstanceCb?.invoke(instance!!);
        }
    }
}
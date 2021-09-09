package engineer.trustmeimansoftware.adlib.jsinterface

import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivity

/**
 * @interface IJavaScriptInterfaceBuilder
 *
 * build instances of JavaScriptInterfaces
 */
interface IJavaScriptInterfaceBuilder {
    /**
     * @param activity - the activity which uses the interface in its webview
     */
    fun build(activity: IAdFullscreenActivity) : AbstractJavaScriptInterface
}
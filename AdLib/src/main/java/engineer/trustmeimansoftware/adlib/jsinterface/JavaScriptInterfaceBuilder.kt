package engineer.trustmeimansoftware.adlib.jsinterface

import engineer.trustmeimansoftware.adlib.IAdFullscreenActivity

/**
 * buils javascript interfaces
 */
class JavaScriptInterfaceBuilder: IJavaScriptInterfaceBuilder {
    override fun build(activity: IAdFullscreenActivity): AbstractJavaScriptInterface {
        return JavaScriptInterface(activity)
    }
}
package engineer.trustmeimansoftware.adlib.jsinterface

import engineer.trustmeimansoftware.adlib.IAdFullscreenActivity

class JavaScriptInterfaceBuilder: IJavaScriptInterfaceBuilder {
    override fun build(activity: IAdFullscreenActivity): AbstractJavaScriptInterface {
        return JavaScriptInterface(activity)
    }
}
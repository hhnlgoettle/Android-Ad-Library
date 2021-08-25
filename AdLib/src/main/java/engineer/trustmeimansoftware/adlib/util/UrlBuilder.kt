package engineer.trustmeimansoftware.adlib.util

import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult

class UrlBuilder {
    companion object {
        fun adRequest(adRequest: AdRequest): String {
            return AdManager.instance?.baseUrl + "/adrequest/"+adRequest.appId+"/"+adRequest.displayId
        }

        fun adImpression(adRequestResult: AdRequestResult): String {
            return AdManager.instance?.baseUrl + "/impression/"+adRequestResult.impressionId
        }
    }
}
package engineer.trustmeimansoftware.adlib.ad

data class AdRequestResult(val adRequest: AdRequest, val adID: String, val ad: InteractionRewardedAd, val downloadUrls: Array<String>)

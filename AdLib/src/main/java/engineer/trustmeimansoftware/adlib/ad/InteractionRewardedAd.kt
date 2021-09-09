package engineer.trustmeimansoftware.adlib.ad

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.callback.OnUserRewardedListener
import engineer.trustmeimansoftware.adlib.reward.RewardItem

/**
 * models an ad that rewards users for watching an ad and interacting with an ad
 * @property rewardType Type of the additional Reward
 * @property rewardAmount max amount user can get through interactions
 * @property additionalReward the additional reward for the user
 */
class InteractionRewardedAd(id: String,
                            url: String,
                            var rewardType: String = "Rewards",
                            var rewardAmount: Long = 10L,
                            requestResult: AdRequestResult? = null,
                            var additionalReward: RewardItem? = null
) : FullscreenAd(id, url, requestResult) {
    var onUserRewardedListener: OnUserRewardedListener? = null

    /**
     * display the ad
     */
    override fun show(activity: AppCompatActivity) {
        if(onUserRewardedListener == null) {
            throw Error("onUserRewardedListener is null")
        }
        Log.d("InteractionRewardedAd", "show()")
        super.show(activity)
    }

    companion object {
        // static inheritance is disallowed in kotlin
        // https://stackoverflow.com/questions/39303180/kotlin-how-can-i-create-a-static-inheritable-function
        /**
         * loads the ad
         */
        fun load(activity: AppCompatActivity, adRequest: AdRequest, adLoadCallback: AdLoadCallback) {
            if(adRequest.type == null) adRequest.type = this::class.java.declaringClass.toString()
            FullscreenAd.load(activity, adRequest, adLoadCallback)
        }
    }
}
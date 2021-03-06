package engineer.trustmeimansoftware.adlib.callback

import engineer.trustmeimansoftware.adlib.reward.RewardItem

interface OnUserRewardedListener {

    /**
     *
     * called when the user earned a reward through watching an InteractionRewardedAd
     */
    fun onRewardEarned(rewards: Array<RewardItem>)
}
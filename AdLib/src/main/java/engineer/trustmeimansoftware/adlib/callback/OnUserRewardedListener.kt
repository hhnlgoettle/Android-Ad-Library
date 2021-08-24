package engineer.trustmeimansoftware.adlib.callback

import engineer.trustmeimansoftware.adlib.reward.RewardItem

interface OnUserRewardedListener {

    /**
     * onRewardEarned
     *
     * called when the user earned a reward through watching an InteractionRewardedAd
     */
    // TODO update Signature: add RewardItem(s)
    fun onRewardEarned(rewards: Array<RewardItem>)
}
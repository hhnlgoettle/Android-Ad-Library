package engineer.trustmeimansoftware.adlib.reward

/**
 * models a reward item
 */
data class RewardItem(val type: String = "Reward", val amount: Long = 10, val isExtraReward: Boolean = false)

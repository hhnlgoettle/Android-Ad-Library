package engineer.trustmeimansoftware.adlib.stats

/**
 * models one interaction between user and ad
 */
data class AdInteraction(
    val timestamp: Long? = null,
    val posX: Long? = null,
    val posY: Long? = null,
    val target: String? = null)

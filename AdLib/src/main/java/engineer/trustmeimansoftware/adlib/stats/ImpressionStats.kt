package engineer.trustmeimansoftware.adlib.stats

import com.google.gson.Gson
import engineer.trustmeimansoftware.adlib.jsonutil.JSONUtil
import org.json.JSONObject

/**
 * models ad impression stats that were collected during ad impression
 */
data class ImpressionStats(
    val hasEarnedReward: Boolean = false,
    val duration: Long? = 0,
    val desiredDuration: Long? = 0,
    val desiredInteractionCount: Long? = 0,
    val allInteractions: Array<AdInteraction>? = arrayOf(),
    val validInteractions: Array<AdInteraction>? = arrayOf(),
    val rewardPercentage: Int? = 0
) {
    companion object {
        fun fromJSON(jsonStr: String): ImpressionStats {
            val jsonObj = JSONObject(jsonStr)
            val hasEarnedReward = JSONUtil.readBoolean(jsonObj, "hasEarnedReward", false)
            val duration = JSONUtil.readNullableLong(jsonObj, "duration", null)
            val desiredDuration = JSONUtil.readNullableLong(jsonObj, "desiredDuration", null)
            val desiredInteractionCount = JSONUtil.readNullableLong(jsonObj, "desiredInteractionCount", null)
            val allInteractions = ImpressionStats.parseInteractions(jsonObj, "allInteractions")
            val validInteractions = ImpressionStats.parseInteractions(jsonObj, "validInteractions")
            val rewardPercentage = JSONUtil.readNullableInt(jsonObj, "rewardPercentage", null)
            return ImpressionStats(
                hasEarnedReward, duration, desiredDuration, desiredInteractionCount, allInteractions, validInteractions, rewardPercentage
            )
        }

        private fun parseInteractions(jsonObj: JSONObject, key: String) : Array<AdInteraction> {
            if(jsonObj.isNull(key)) return arrayOf()
            val jsonArray = jsonObj.getJSONArray(key)
            val adInteractionArray: MutableList<AdInteraction> = mutableListOf()
            for (i in 0 until jsonArray.length()) {
                val jsonItem = jsonArray.getJSONObject(i)
                val timestamp = JSONUtil.readNullableLong(jsonItem, "timestamp", null)
                val posX = JSONUtil.readNullableLong(jsonItem, "posX", null)
                val posY = JSONUtil.readNullableLong(jsonItem, "posY", null)
                val target = JSONUtil.readNullableString(jsonItem, "target", null)
                adInteractionArray.add(AdInteraction(timestamp, posX, posY, target))
            }
            return adInteractionArray.toTypedArray()
        }

        fun toJSONString(impressionStats: ImpressionStats): String {
            var gson = Gson()
            return gson.toJson(impressionStats);
        }
    }
}
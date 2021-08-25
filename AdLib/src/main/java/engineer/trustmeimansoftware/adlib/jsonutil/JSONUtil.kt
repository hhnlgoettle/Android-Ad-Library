package engineer.trustmeimansoftware.adlib.jsonutil

import org.json.JSONArray
import org.json.JSONObject

class JSONUtil {
    companion object {
        fun readNullableString(jsonObj: JSONObject, key: String, fallback: String? = null): String? {
            if(jsonObj.isNull(key)) {
                return fallback
            }
            return jsonObj.getString(key)
        }
        fun readBoolean(jsonObj: JSONObject, key: String, fallback: Boolean): Boolean {
            if(jsonObj.isNull(key)) {
                return fallback
            }
            return jsonObj.getBoolean(key)
        }
        fun readNullableLong(jsonObj: JSONObject, key: String, fallback: Long? = null): Long? {
            if(jsonObj.isNull(key)) {
                return fallback
            }
            return jsonObj.getLong(key)
        }
        fun readNullableInt(jsonObj: JSONObject, key: String, fallback: Int? = null): Int? {
            if(jsonObj.isNull(key)) {
                return fallback
            }
            return jsonObj.getInt(key)
        }
        fun readNullableJSONArray(jsonObj: JSONObject, key: String, fallback: JSONArray? = null): JSONArray? {
            if(jsonObj.isNull(key)) {
                return fallback
            }
            return jsonObj.getJSONArray(key)
        }

        fun jsonArrayToStringArray(jsonArray: JSONArray): Array<String> {
            val array = ArrayList<String>();
            for (i in 0 until jsonArray.length()) {
                array.add(jsonArray.getString(i))
            }
            return array.toTypedArray()
        }

    }
}
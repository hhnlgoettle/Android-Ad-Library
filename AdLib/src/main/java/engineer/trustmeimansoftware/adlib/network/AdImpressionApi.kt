package engineer.trustmeimansoftware.adlib.network


import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.Ad

import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import engineer.trustmeimansoftware.adlib.util.UrlBuilder
import org.json.JSONObject

import java.lang.Exception
import java.nio.charset.Charset
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * Api for WebServer for AdImpressions
 */
class AdImpressionApi {
    companion object {
        /**
         * sends an [ImpressionStats] to webserver
         */
        suspend fun sendAdImpression(ad: Ad, stats: ImpressionStats) = suspendCoroutine<Boolean> { cont ->
            try {// create network queue
                val queue = Volley.newRequestQueue(AdManager.instance!!.context)
                val url = UrlBuilder.adImpression(ad.requestResult!!)

                // convert impression to json
                val data = ImpressionStats.toJSONString(stats)
                // request body
                val jsonBody = JSONObject()
                // put impression data under key data
                jsonBody.put("data", JSONObject(data))

                // build request
                val request: StringRequest = object: StringRequest(
                    Method.POST, url,
                    { _ ->
                        cont.resume(true)
                    },
                    { error ->
                        // request failed
                        cont.resumeWithException(error)
                    },
                )  {
                    // returns the body as byte array
                    override fun getBody(): ByteArray {
                        return jsonBody.toString().toByteArray(Charset.defaultCharset())
                    }

                    // set content type correctly
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }
                }

                // add request to queue
                queue.add(request)
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }

    }
}

package engineer.trustmeimansoftware.adlib.network

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import engineer.trustmeimansoftware.adlib.util.UrlBuilder
import org.json.JSONObject
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
        // TODO return AdImpressionResult
        suspend fun sendAdImpression(ad: Ad, stats: ImpressionStats) = suspendCoroutine<AdRequestResult> { cont ->
            // create network queue
            val queue = Volley.newRequestQueue(AdManager.instance!!.context)
            val url = UrlBuilder.adImpression(ad.requestResult!!)

            // convert impression to json
            val body = ImpressionStats.toJSONString(stats)
            // request body
            val jsonBody = JSONObject()
            // put impression data under key data
            jsonBody.put("data", JSONObject(body))

            // build request
            val request: StringRequest = object: StringRequest(
                Method.POST, url,
                { response ->
                    cont.resume(AdRequestResult.fromJSONString(response))
                },
                { error ->
                    cont.resumeWithException(error)
                }
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
        }
    }
}

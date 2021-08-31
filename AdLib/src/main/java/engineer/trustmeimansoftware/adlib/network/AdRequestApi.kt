package engineer.trustmeimansoftware.adlib.network

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.jsonutil.JSONUtil
import engineer.trustmeimansoftware.adlib.util.UrlBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Api that handles requests for loading ads
 */
class AdRequestApi {
    companion object {
        suspend fun loadAdRequest(adRequest: AdRequest) = suspendCoroutine<AdRequestResult> { cont ->
            val queue = Volley.newRequestQueue(AdManager.instance!!.context)
            val url = UrlBuilder.adRequest(adRequest)
            val cachedAdsAsJSON = JSONArray(JSONUtil.toJSONString(adRequest.cachedAds))
            val jsonBody = JSONObject()
            jsonBody.put("cachedCreatives", cachedAdsAsJSON)
            val stringRequest = object: StringRequest(
                Method.POST, url,
                { response ->
                    cont.resume(AdRequestResult.fromJSONString(response))
                },
                { error ->
                    cont.resumeWithException(error)
                }
            )
            {
                // returns the body as byte array
                override fun getBody(): ByteArray {
                    return jsonBody.toString().toByteArray(Charset.defaultCharset())
                }

                // set content type correctly
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }
            queue.add(stringRequest)
        }
    }
}

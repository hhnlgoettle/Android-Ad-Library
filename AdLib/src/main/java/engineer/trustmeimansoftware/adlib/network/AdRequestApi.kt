package engineer.trustmeimansoftware.adlib.network

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.util.UrlBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AdRequestApi {
    companion object {
        suspend fun loadAdRequest(adRequest: AdRequest) = suspendCoroutine<AdRequestResult> { cont ->
            val queue = Volley.newRequestQueue(AdManager.instance!!.context)
            val url = UrlBuilder.adRequest(adRequest)
            val stringRequest = StringRequest(
                Request.Method.POST, url,
                { response ->
                    cont.resume(AdRequestResult.fromJSONString(response))
                },
                { error ->
                    cont.resumeWithException(error)
                }
            )
            queue.add(stringRequest)
        }
    }
}

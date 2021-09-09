package engineer.trustmeimansoftware.adlib.worker

import android.content.Context
import androidx.work.*
import com.android.volley.VolleyError
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequestResult
import engineer.trustmeimansoftware.adlib.database.impression.getDatabase
import engineer.trustmeimansoftware.adlib.network.AdImpressionApi
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.TimeUnit

const val BAD_REQUEST: Int = 400
const val CONFLICT: Int = 409
const val NOT_FOUND: Int = 404

/**
 * a [Worker] that uploads saved [engineer.trustmeimansoftware.adlib.stats.ImpressionStats] to the AdWebserver
 *<p> will delete ImpressionStats from the [engineer.trustmeimansoftware.adlib.database.impression.ImpressionDatabase] if sending was successful
 *
 *
 */
class UploadImpressionWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    /**
     * executes the task
     */
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            // retrieve database
            val db = getDatabase(applicationContext)

            // get all unsent items
            val unsent = db.impressionDao.getUnsent()

            // no items present? great! we can stop here
            if(unsent.isEmpty()) {
                return@withContext Result.success()
            }

            unsent.forEach {
                    item ->
                var isSent = false
                val stats = ImpressionStats.fromJSON(item.data)
                val ad = Ad("", "", AdRequestResult(item.impressionId, "", "","", arrayOf()))
                try {
                    AdImpressionApi.sendAdImpression(ad, stats)
                    // no error thrown -> success
                    println("### no error")
                    isSent = true
                }
                catch(ex: VolleyError) {
                    println("### volleyerror $ex")
                    ex.networkResponse?.let {
                        when(it.statusCode) {
                            // if one of these errors occur, all subsequent request will fail too
                                // so we can spare us the hassle and delete them right away
                            BAD_REQUEST, CONFLICT, NOT_FOUND -> {
                                isSent = true
                            }
                            else -> {}
                        }
                    }
                }
                catch (ex: Exception) {
                    // ignore other exceptions
                }
                if(isSent) {
                    db.impressionDao.deleteById(item.impressionId)
                }
            }

            return@withContext Result.success()
        }
    }
}

/**
 * enqueues a unique [UploadImpressionWorker] to the [WorkManager]
 * <p>if there is already a Worker, it will be kept
 * <p>the ID for this unique Worker is IRA_UploadImpressionWorker
 */
fun enqueueUploadImpressionWorkerToWorkManager(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    val work = PeriodicWorkRequestBuilder<UploadImpressionWorker>(2, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    val workManager = WorkManager.getInstance(context)
    workManager.enqueueUniquePeriodicWork(
        "IRA_UploadImpressionWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        work)
}

package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.TestWorkerBuilder
import engineer.trustmeimansoftware.adlib.database.impression.ImpressionDatabase
import engineer.trustmeimansoftware.adlib.database.impression.ImpressionDbItem
import engineer.trustmeimansoftware.adlib.database.impression.getDatabase
import engineer.trustmeimansoftware.adlib.manager.AdManager
import engineer.trustmeimansoftware.adlib.worker.UploadImpressionWorker
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.Setup
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@RunWith(AndroidJUnit4::class)
class UploadImpressionWorkerTest {

    lateinit var server: MockWebServer
    @Before
    fun before() {
        server = MockWebServer()
        server.dispatcher = object: Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println("### request: "+request.path)
                if(request.requestLine.contains("500")) {
                    return MockResponse().setResponseCode(500)
                } else if(request.requestLine.contains("404")) {
                    return MockResponse().setResponseCode(404)
                }
                else if(request.requestLine.contains("409")) {
                    return MockResponse().setResponseCode(409)
                }
                else if(request.requestLine.contains("400")) {
                    return MockResponse().setResponseCode(400)
                }

                return MockResponse().setResponseCode(200)
            }

        }
    }

    @After
    fun after() {
        server.close()
    }

    @Test()
    fun sending_succeeds_deletes_from_db() {

        val latch = CountDownLatch(1)
        val latch2 = CountDownLatch(1)
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            latch.countDown()
        }

        latch.await()

        val context = ApplicationProvider.getApplicationContext<Context>()

        val db = getDatabase(context)
        db.impressionDao.insert(ImpressionDbItem("200", "{}", false))

        // manipulate url
        val url = server.url("")
        AdManager.instance!!.config.baseUrl = url.toString()

        // start worker
        val worker = TestListenableWorkerBuilder<UploadImpressionWorker>(
            context = context
        ).build()


        var unsent: List<ImpressionDbItem> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            worker.doWork()
            unsent = db.impressionDao.getUnsent()
            latch2.countDown()
        }

        latch2.await()
        assertEquals(0, unsent.size)
    }

    @Test()
    fun sending_fails_500_no_delete() {

        val latch = CountDownLatch(1)
        val latch2 = CountDownLatch(1)
        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )

        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            latch.countDown()
        }

        latch.await()

        val context = ApplicationProvider.getApplicationContext<Context>()

        val db = getDatabase(context)
        db.impressionDao.insert(ImpressionDbItem("500", "{}", false))

        // manipulate url
        val url = server.url("")
        AdManager.instance!!.config.baseUrl = url.toString()

        // start worker
        val worker = TestListenableWorkerBuilder<UploadImpressionWorker>(
            context = context
        ).build()


        var unsent: List<ImpressionDbItem> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            worker.doWork()
            unsent = db.impressionDao.getUnsent()
            latch2.countDown()
        }

        latch2.await()
        assertEquals(1, unsent.size)
    }
}
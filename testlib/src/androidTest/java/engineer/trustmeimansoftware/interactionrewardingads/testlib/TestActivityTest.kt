package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.AdManager
import junit.framework.TestCase.assertEquals
import org.junit.Test
class TestActivityTest {


    @Test
    fun launchTestActivity_initsAdManager() {
        val scenario = ActivityScenario.launch(TestActivity::class.java)
        scenario.onActivity() {
            assertEquals(it.adManager, AdManager.instance)
            assertEquals(it.adManager != null, true)
            assertEquals(it.adManager?.adFullscreenActivityBuilder != null, true)
        }
    }

    @Test
    fun launchWithTestAdFullscreenActivityBuilder_initsCustomBuilder() {
        val scenarioIntent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, TestActivity::class.java)
        val myBundle = Bundle()
        myBundle.putBoolean(TestActivity.optCustomAdFullscreenBuilder, true)
        scenarioIntent.putExtras(myBundle)
        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity {
            assertEquals(
                it.adManager?.adFullscreenActivityBuilder is TestAdFullscreenActivityBuilder,
                true
            )
        }
    }
}
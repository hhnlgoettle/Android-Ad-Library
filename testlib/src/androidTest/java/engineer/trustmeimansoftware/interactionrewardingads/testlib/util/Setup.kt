package engineer.trustmeimansoftware.interactionrewardingads.testlib.util

import android.content.Intent
import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.interactionrewardingads.testlib.TestActivity

class Setup {
    companion object {
        fun setupScenarioIntent(options: Array<String>? = null): Intent {
            val scenarioIntent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, TestActivity::class.java)
            val myBundle = Bundle()
            options?.let {
                for (i in options.indices) {
                    myBundle.putBoolean(options[i], true)
                }
            }
            scenarioIntent.putExtras(myBundle)
            return scenarioIntent
        }
    }

}
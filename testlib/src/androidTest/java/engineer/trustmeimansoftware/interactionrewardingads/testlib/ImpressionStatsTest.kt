package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import engineer.trustmeimansoftware.adlib.stats.ImpressionStats
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader

@RunWith(AndroidJUnit4::class)
class ImpressionStatsTest {

    fun readJSONFromFile(fileName: String): String {
        var reader: BufferedReader? = null
        try {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val file = appContext.assets.open(fileName)

            reader = file.bufferedReader()
            return reader.readText()
        } finally {
            reader?.close()
        }
    }

    @Test(timeout = 3000)
    fun parseJSONtoImpressionStats() {
        val jsonStr = readJSONFromFile("json/impressionStats.json")
        val impressionStats = ImpressionStats.fromJSON(jsonStr)
        assertEquals(true, impressionStats.hasEarnedReward)
        assertEquals(5, impressionStats.allInteractions?.size)
        assertEquals(3, impressionStats.validInteractions?.size)
        assertEquals(null, impressionStats.allInteractions?.get(0)?.target)
    }
}
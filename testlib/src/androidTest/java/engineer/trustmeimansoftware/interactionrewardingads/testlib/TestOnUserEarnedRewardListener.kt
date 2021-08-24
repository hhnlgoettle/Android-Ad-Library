package engineer.trustmeimansoftware.interactionrewardingads.testlib

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.cache.CacheManager
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback
import engineer.trustmeimansoftware.adlib.callback.OnUserRewardedListener
import engineer.trustmeimansoftware.adlib.reward.RewardItem
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.AdManagerUtil
import engineer.trustmeimansoftware.interactionrewardingads.testlib.util.Setup
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Error
import java.lang.Exception
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class OnUserEarnedRewardListenerTest {

    @Test(timeout = 5000)
    fun testInterface_onStart() {

        val latch = CountDownLatch(1)

        val scenarioIntent = Setup.setupScenarioIntent(
            arrayOf()
        )
        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(scenarioIntent)
        scenario.onActivity { activity ->
            val manager = AdManagerUtil(activity.adManager!!)
            activity.ad = InteractionRewardedAd(
                "onClose_success",
                manager.cache.getFullCreativePath("onClose_success"))
            activity.ad!!.rewardAmount = 120L
            activity.ad!!.rewardType = "Coin"
            activity.ad!!.fullscreenContentCallback = object: FullscreenContentCallback {
                override fun onDismissed() {}
                override fun onFailedToShow(error: Error) {
                    assertEquals(null, error)
                }
            }
            activity.ad!!.onUserRewardedListener = object: OnUserRewardedListener {
                override fun onRewardEarned(rewards: Array<RewardItem>) {
                    assertEquals(1, rewards.size)
                    assertEquals(120L, rewards[0].amount)
                    assertEquals("Coin", rewards[0].type)
                    assertEquals(false, rewards[0].isExtraReward)
                    latch.countDown()
                }
            }

            activity.displayAd()
        }
        try {
            latch.await()
        } catch (e: Exception) {
        }
    }

}
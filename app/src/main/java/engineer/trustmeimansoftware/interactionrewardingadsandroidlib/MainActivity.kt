package engineer.trustmeimansoftware.interactionrewardingadsandroidlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import engineer.trustmeimansoftware.adlib.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.AdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.AdManager
import engineer.trustmeimansoftware.adlib.ad.Ad
import engineer.trustmeimansoftware.adlib.ad.AdRequest
import engineer.trustmeimansoftware.adlib.ad.InteractionRewardedAd
import engineer.trustmeimansoftware.adlib.callback.AdLoadCallback
import engineer.trustmeimansoftware.adlib.callback.FullscreenContentCallback
import engineer.trustmeimansoftware.adlib.callback.OnUserRewardedListener
import engineer.trustmeimansoftware.adlib.reward.RewardItem
import java.lang.Error

/**
 * A demo Activity used to debug and showcase the AdLib
 */
class MainActivity : AppCompatActivity() {
    private lateinit var btnStartAd: Button
    private var interactionRewardedAd: InteractionRewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AdManager.build(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartAd = findViewById(R.id.btnStartAd)
        btnStartAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        if(interactionRewardedAd == null) {
            val adRequest: AdRequest = AdRequest.build("6123df5155456a00388a9d5e")
            InteractionRewardedAd.load(this, adRequest, object: AdLoadCallback {
                override fun onAdLoaded(ad: Ad) {
                    if(ad is InteractionRewardedAd) {
                        Log.d("MainActivity", "Ad loaded successfully")
                        interactionRewardedAd = ad
                    } else {
                        throw Error("onAdLoaded received not an InteractionRewardedAd")
                    }
                }

                override fun onAdFailedToLoad(error: Error) {
                    Log.d("MainActivity", "Error: $error")
                }
            })
        } else {
            interactionRewardedAd?.let {
                it.fullscreenContentCallback = object: FullscreenContentCallback{
                    override fun onDismissed() {
                        Log.d("MainActivity", "onDismissed")
                        interactionRewardedAd = null
                    }

                    override fun onFailedToShow(error: Error) {
                        Log.d("MainActivity", "onFailedToShow: $error")
                    }
                }
                it.onUserRewardedListener = object : OnUserRewardedListener {
                    override fun onRewardEarned(rewards: Array<RewardItem>) {
                        Log.d("MainActivity", "User received reward")
                    }
                }
                it.show(this)
            }
        }
    }
}
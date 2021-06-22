package engineer.trustmeimansoftware.interactionrewardingadsandroidlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import engineer.trustmeimansoftware.adlib.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.AdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.AdManager

/**
 * A demo Activity used to debug and showcase the AdLib
 */
class MainActivity : AppCompatActivity() {
    private lateinit var btnStartAd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        AdFullscreenActivityBuilder.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartAd = findViewById(R.id.btnStartAd)
        btnStartAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        val intent = AdFullscreenActivityBuilder.buildIntent(this)
        AdFullscreenActivityBuilder.launchIntent(intent)
    }
}
package engineer.trustmeimansoftware.adlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button

class AdFullscreenActivity : AppCompatActivity() {
    private lateinit var btnCloseAd: Button
    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adfullscreen)

        btnCloseAd = findViewById(R.id.btnCloseAd)
        webview = findViewById(R.id.webview)
        
        btnCloseAd.setOnClickListener {
            this.finish()
        }
    }
}
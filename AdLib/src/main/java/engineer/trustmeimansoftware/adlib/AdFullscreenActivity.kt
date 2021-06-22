package engineer.trustmeimansoftware.adlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdFullscreenActivity : AppCompatActivity() {
    lateinit var btnCloseAd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adfullscreen)

        btnCloseAd = findViewById(R.id.btnCloseAd)
        btnCloseAd.setOnClickListener {
            this.finish()
        }
    }
}
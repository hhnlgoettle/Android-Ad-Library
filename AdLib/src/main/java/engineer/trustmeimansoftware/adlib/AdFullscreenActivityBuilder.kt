package engineer.trustmeimansoftware.adlib

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/**
 * class AdFullScreenActivityBuilder
 *
 * create AdActivities and receive their result on completion
 *
 */
open class AdFullscreenActivityBuilder(activity: AppCompatActivity) :IAdFullscreenActivityBuilder {

    init {
        initialize(activity)
    }

    private val TAG = "AdFullscreenActivityBuilder"

    private var launchActivity: ActivityResultLauncher<Intent>? = null;

    /**
     * initializes an ActivityResultLauncher for launching activities
     *
     * call this method with the correct AppCompatActivity
     * from which you want to launch the AdActivity
     */
    @SuppressLint("LongLogTag")
    final override fun initialize(activity: AppCompatActivity) {
        launchActivity = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
                result ->
            Log.d(TAG, "Result received from launched Activity: $result")
        }
    }

    /**
     * create an intent used to launch an AdActivity
     */
    override fun buildIntent(activity: AppCompatActivity): Intent {
        val intent: Intent = Intent(activity, AdFullscreenActivity::class.java).apply {
        }
        val bundle = Bundle()
        intent.putExtras(bundle)
        return intent
    }

    /**
     * launch an intent to
     */
    @SuppressLint("LongLogTag")
    override fun launchIntent(intent: Intent) {
        Log.d("AdFullscreenActivityBuilder", "launching intent")
        if(launchActivity == null) throw Error("launchActivity is null. Did you call AdFullscreenActivityBuilder.initialize()?")
        launchActivity?.launch(intent)
    }
}
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
class AdFullscreenActivityBuilder {

    val TAG = "AdFullscreenActivityBuilder"

    companion object {
        const val TAG = "AdFullscreenActivityBuilder"
        private var launchActivity: ActivityResultLauncher<Intent>? = null;


        /**
         * initializes an ActivityResultLauncher for launching activities
         *
         * call this method with the correct AppCompatActivity
         * from which you want to launch the AdActivity
         */
        @SuppressLint("LongLogTag")
        fun initialize(activity: AppCompatActivity) {
            launchActivity = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) {
                    result ->
                    Log.d(TAG, "Result received from launched Activity: $result")
                }
        }

        /**
         * create an intent used to launch an AdActivity
         */
        fun buildIntent(activity: AppCompatActivity): Intent {
            val intent: Intent = Intent(activity, AdFullscreenActivity::class.java).apply {
            }
            val bundle = Bundle()
            intent.putExtras(bundle)
            return intent
        }

        /**
         * launch an intent to
         */
        fun launchIntent(intent: Intent) {
            if(launchActivity == null) throw Error("launchActivity is null. Did you call AdFullscreenActivityBuilder.initialize()")
            launchActivity?.launch(intent)
        }
    }


}
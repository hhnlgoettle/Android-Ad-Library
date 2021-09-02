package engineer.trustmeimansoftware.interactionrewardingads.testlib

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import engineer.trustmeimansoftware.adlib.adactivity.AdFullscreenActivity
import engineer.trustmeimansoftware.adlib.adactivity.IAdFullscreenActivityBuilder
import engineer.trustmeimansoftware.adlib.ad.Ad

/**
 *
 * @implements
 */
class TestAdFullscreenActivityBuilder(activity: AppCompatActivity)
    : IAdFullscreenActivityBuilder {

    companion object {
        const val TAG = "TestAdFullscreenActivityBuilder"
    }

    init{
        initialize(activity)
    }

    private var launchActivity: ActivityResultLauncher<Intent>? = null

    var lastResult: ActivityResult? = null
    var onResultCallback: ((ActivityResult) -> Unit)? = null
    var data : Intent? = null
    var code : Int? = null
    var activityClass: Class<*> = AdFullscreenActivity::class.java


    @SuppressLint("LongLogTag")
    override fun initialize(activity: AppCompatActivity) {
        try {
            launchActivity = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) {
                    result ->
                lastResult = result
                data = result.data
                code = result.resultCode
                Log.d(TAG, "received result: $result")
                onResultCallback?.let {
                    it(result)
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun buildIntent(activity: AppCompatActivity): Intent {
        return Intent(activity, activityClass)
    }

    fun buildIntent(activity: AppCompatActivity, ad: Ad): Intent {
        val intent = Intent(activity, activityClass)
        val bundle = Bundle()
        bundle.putString("EXTRA_AD_ID", ad.getID())
        intent.putExtras(bundle)
        return intent
    }


    override fun launchIntent(intent: Intent) {
        if(launchActivity == null) throw Error("launchActivity is null. Did you call AdFullscreenActivityBuilder.initialize()?")
        launchActivity?.launch(intent)
    }

}
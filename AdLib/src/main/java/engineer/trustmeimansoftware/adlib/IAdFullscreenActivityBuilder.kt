package engineer.trustmeimansoftware.adlib

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

interface IAdFullscreenActivityBuilder {

    /**
     * initialize builder
     */
    fun initialize(activity: AppCompatActivity)

    /**
     * create an intent used to launch an AdActivity
     */
    fun buildIntent(activity: AppCompatActivity): Intent

    /**
     * launch an intent to
     */
    @SuppressLint("LongLogTag")
    fun launchIntent(intent: Intent)
}
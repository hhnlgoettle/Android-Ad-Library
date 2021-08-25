package engineer.trustmeimansoftware.adlib.util

import android.content.Context
import android.content.pm.PackageManager


class AppId {
    companion object {
        fun getAppIdFromContext(context: Context): String {
            val app = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val bundle = app.metaData
            val appId = bundle.getString("ira_appId");
            if(appId == null) {
                throw Error("appId is not set in manifest meta data. Set your appId with key 'ira_appId'")
            }
            return appId;
        }
    }
}
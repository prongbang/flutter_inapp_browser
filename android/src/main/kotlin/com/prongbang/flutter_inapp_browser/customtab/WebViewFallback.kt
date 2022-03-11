package com.prongbang.flutter_inapp_browser.customtab

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Browser
import android.util.Log

class WebViewFallback(
    private val activity: Activity,
    private val onOpenWebView: () -> Unit = {},
    private val onOpenCustomTab: () -> Unit = {},
    private var browserPackages: List<String> = listOf(
        "com.huawei.browser",
        "com.android.browser",
        "com.android.chrome"
    )
) : CustomTabFallback {

    override fun openUri(context: Context, uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            val resolveInfo =
                activity.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            var browserPackageName: String? = null
            if (resolveInfo?.activityInfo != null) {
                browserPackageName = resolveInfo.activityInfo.packageName
                Log.d(
                    WebViewFallback::class.java.simpleName,
                    "Using browser in package $browserPackageName"
                )
            }

            if (browserPackages.contains(browserPackageName)) {
                intent.setPackage(browserPackageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackageName)
            }

            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            onOpenWebView.invoke()
        }
    }

    override fun onOpen() {
        onOpenCustomTab.invoke()
    }
}
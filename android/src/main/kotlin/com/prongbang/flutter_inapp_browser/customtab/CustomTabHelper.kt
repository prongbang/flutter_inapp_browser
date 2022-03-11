package com.prongbang.flutter_inapp_browser.customtab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.prongbang.flutter_inapp_browser.R

class CustomTabsHelper {
    private var customTabsSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null
    private var connectionCallback: ConnectionCallback? = null

    /**
     * Unbinds the Activity from the Custom Tabs Service
     *
     * @param activity the activity that is connected to the service
     */
    fun unbindCustomTabsService(activity: Activity) {
        if (connection == null) {
            return
        }
        try {
            connection?.let {
                activity.unbindService(it)
            }
        } catch (e: Exception) {
            Log.e(CustomTabsHelper::class.java.simpleName, "${e.message}")
        }
        client = null
        customTabsSession = null
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession
     *
     * @return a CustomTabsSession
     */
    val session: CustomTabsSession?
        get() {
            if (client == null) {
                customTabsSession = null
            } else if (customTabsSession == null) {
                customTabsSession = client!!.newSession(null)
            }
            return customTabsSession
        }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
     */
    fun setConnectionCallback(connectionCallback: ConnectionCallback?) {
        this.connectionCallback = connectionCallback
    }

    /**
     * Binds the Activity to the Custom Tabs Service
     *
     * @param activity the activity to be bound to the service
     */
    fun bindCustomTabsService(activity: Activity?) {
        if (client != null || activity == null) {
            return
        }
        val packageName = CustomTabsPackageHelper.getPackageNameToUse(activity) ?: return
        connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient
            ) {
                this@CustomTabsHelper.client = client
                this@CustomTabsHelper.client?.warmup(0L)
                connectionCallback?.onConnected()

                // Initialize a session as soon as possible.
                session
            }

            override fun onServiceDisconnected(name: ComponentName) {
                client = null
                connectionCallback?.onDisconnected()
            }

            override fun onBindingDied(name: ComponentName) {
                client = null
                connectionCallback?.onDisconnected()
            }
        }
        connection?.let {
            CustomTabsClient.bindCustomTabsService(activity, packageName, it)
        }
    }

    fun mayLaunchUrl(uri: Uri?, extras: Bundle?, otherLikelyBundles: List<Bundle?>?): Boolean {
        if (client == null) {
            return false
        }
        val session = session
        return session != null && session.mayLaunchUrl(uri!!, extras, otherLikelyBundles)
    }

    companion object {
        private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "androidx.browser.customtabs.extra.KEEP_ALIVE"

        /**
         * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView
         *
         * @param context          The host activity
         * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
         * @param uri              the Uri to be opened
         * @param fallback         a CustomTabFallback to be used if Custom Tabs is not available
         */
        fun openCustomTab(
            context: Context, customTabsIntent: CustomTabsIntent, uri: Uri,
            fallback: CustomTabFallback? = null
        ) {

            try {
                val packageName = CustomTabsPackageHelper.getPackageNameToUse(context)
                if (packageName == null) {
                    fallback?.openUri(context, uri)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        customTabsIntent.intent.putExtra(
                            Intent.EXTRA_REFERRER, Uri.parse(
                                Intent.URI_ANDROID_APP_SCHEME.toString() + "//" + context.packageName
                            )
                        )
                    }
                    fallback?.onOpen()
                    customTabsIntent.intent.setPackage(packageName)
                    customTabsIntent.launchUrl(context, uri)
                }
            } catch (e: ActivityNotFoundException) {
                fallback?.openUri(context, uri)
            }
        }

        @SuppressLint("Range")
        fun openToLeft(
            activity: Activity?,
            options: CustomTabsOptions,
            onOpenWebView: () -> Unit = {},
            onOpenCustomTab: () -> Unit = {}
        ) {
            activity?.let { it ->
                val customTabsIntent = CustomTabsIntentBuilder(it).createCustomTabsToLeftIntent(
                    R.drawable.ic_close_black, Color.parseColor(options.toolbarColor)
                )
                val webViewFallback = WebViewFallback(it, onOpenWebView, onOpenCustomTab)
                openCustomTab(it, customTabsIntent, Uri.parse(options.url), webViewFallback)
            }
        }

        @SuppressLint("Range")
        fun openToTop(
            activity: Activity?,
            options: CustomTabsOptions,
            onOpenWebView: () -> Unit = {},
            onOpenCustomTab: () -> Unit = {}
        ) {
            activity?.let { it ->
                val customTabsIntent = CustomTabsIntentBuilder(it).createCustomTabsToTopIntent(
                    R.drawable.ic_close_black, Color.parseColor(options.toolbarColor)
                )
                val webViewFallback = WebViewFallback(it, onOpenWebView, onOpenCustomTab)
                openCustomTab(it, customTabsIntent, Uri.parse(options.url), webViewFallback)
            }
        }

        fun addKeepAliveExtra(context: Context, intent: Intent) {
            val keepAliveIntent = Intent().setClassName(
                context.packageName,
                KeepAliveService::class.java.canonicalName ?: "KeepAliveService"
            )
            intent.putExtra(
                EXTRA_CUSTOM_TABS_KEEP_ALIVE,
                keepAliveIntent
            )
        }
    }
}
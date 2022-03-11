package com.prongbang.flutter_inapp_browser.customtab

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * How to use:
 * Add registerActivityLifecycleCallbacks(CustomTabsActivityLifecycleCallbacks()) in onCreate function under Application class.
 */
class CustomTabsActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

    private var customTabsHelper: CustomTabsHelper? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        customTabsHelper = CustomTabsHelper()
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        customTabsHelper?.bindCustomTabsService(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        activity.let {
            customTabsHelper?.unbindCustomTabsService(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
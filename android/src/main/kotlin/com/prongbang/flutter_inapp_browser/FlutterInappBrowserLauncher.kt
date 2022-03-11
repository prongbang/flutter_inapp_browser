package com.prongbang.flutter_inapp_browser

import android.app.Activity
import com.prongbang.flutter_inapp_browser.customtab.CustomTabsHelper
import com.prongbang.flutter_inapp_browser.customtab.CustomTabsOptions
import io.flutter.plugin.common.MethodChannel

class FlutterInappBrowserLauncher(private val activity: Activity?) {

    fun launcher(arguments: Arguments, result: MethodChannel.Result) {
        val options = CustomTabsOptions(url = arguments.url, toolbarColor = arguments.toolbarColor)
        when (arguments.transition) {
            "to-top" -> {
                CustomTabsHelper.openToTop(activity, options, onOpenWebView = {
                    result.success(false)
                }, onOpenCustomTab = {
                    result.success(true)
                })
            }
            "to-left" -> {
                CustomTabsHelper.openToLeft(activity, options, onOpenWebView = {
                    result.success(false)
                }, onOpenCustomTab = {
                    result.success(true)
                })
            }
            else -> result.success(false)
        }
    }
}
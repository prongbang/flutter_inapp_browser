package com.prongbang.flutter_inapp_browser.customtab

/**
 * A Callback for when the service is connected or disconnected. Use those callbacks to
 * handle UI changes when the service is connected or disconnected
 */
interface ConnectionCallback {
    /**
     * Called when the service is connected
     */
    fun onConnected()

    /**
     * Called when the service is disconnected
     */
    fun onDisconnected()
}
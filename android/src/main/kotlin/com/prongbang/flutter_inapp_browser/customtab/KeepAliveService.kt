package com.prongbang.flutter_inapp_browser.customtab

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Empty service to bind to, raising the application's importance.
 */
@SuppressLint("Registered")
class KeepAliveService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    companion object {
        private val binder = Binder()
    }
}
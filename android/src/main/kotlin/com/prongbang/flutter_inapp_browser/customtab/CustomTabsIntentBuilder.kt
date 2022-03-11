package com.prongbang.flutter_inapp_browser.customtab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.DrawableCompat
import com.prongbang.flutter_inapp_browser.R

class CustomTabsIntentBuilder(private val context: Context) {

    fun createCustomTabsToLeftIntent(
        @DrawableRes drawableId: Int,
        @ColorInt toolbarColor: Int
    ): CustomTabsIntent {
        val customTabsIntent = getDefaultCustomTabsIntentBuilder(drawableId, toolbarColor)
            .setColorSchemeParams(
                CustomTabsIntent.COLOR_SCHEME_DARK,
                getDefaultCustomTabColorSchemeBuilder(toolbarColor)
            )
            .setStartAnimations(context, R.anim.anim_slide_in_right, android.R.anim.fade_out)
            .setExitAnimations(context, android.R.anim.fade_in, R.anim.anim_slide_out_right)
            .build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)

        return customTabsIntent
    }

    fun createCustomTabsToTopIntent(
        @DrawableRes drawableId: Int,
        @ColorInt toolbarColor: Int
    ): CustomTabsIntent {
        val customTabsIntent = getDefaultCustomTabsIntentBuilder(drawableId, toolbarColor)
            .setStartAnimations(context, R.anim.anim_slide_in_bottom, android.R.anim.fade_out)
            .setExitAnimations(context, android.R.anim.fade_in, R.anim.anim_slide_out_bottom)
            .build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)

        return customTabsIntent
    }

    private fun getDefaultCustomTabColorSchemeBuilder(@ColorInt toolbarColor: Int): CustomTabColorSchemeParams {
        return CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor)
            .setSecondaryToolbarColor(toolbarColor)
            .build()
    }

    private fun getDefaultCustomTabsIntentBuilder(
        @DrawableRes drawableId: Int,
        @ColorInt toolbarColor: Int
    ): CustomTabsIntent.Builder {
        val builder = CustomTabsIntent.Builder()
            .setToolbarColor(toolbarColor)
            .setSecondaryToolbarColor(toolbarColor)
            .setShowTitle(true)

        val backArrow = getBitmapFromVectorDrawable(drawableId)
        if (backArrow != null) {
            builder.setCloseButtonIcon(backArrow)
        }
        return builder
    }

    private fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap? {
        var drawable = AppCompatResources.getDrawable(context, drawableId) ?: return null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable)
                .mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
package com.baghdad.novix.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log

object IconSwitcher {

    fun switchAppIcon(context: Context) {
        val packageManager = context.packageManager

        val componentLight = ComponentName(context, "com.baghdad.novix.MainActivityLight")
        val componentDark = ComponentName(context, "com.baghdad.novix.MainActivityDark")

        val isDark = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        try {
            if (isDark) {
                packageManager.setComponentEnabledSetting(
                    componentDark,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                packageManager.setComponentEnabledSetting(
                    componentLight,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            } else {
                packageManager.setComponentEnabledSetting(
                    componentLight,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                packageManager.setComponentEnabledSetting(
                    componentDark,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        } catch (e: Exception) {
            Log.e("IconSwitcher", "Failed to switch app icon", e)
        }
    }
}
package com.veryfi.lens.headless.credit.cards.demo.helpers

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.veryfi.lens.headless.credit.cards.demo.R


object ThemeHelper {

    fun setBackgroundColorToStatusBar(activity: Activity, context: Context) {
        var color = R.color.md_theme_light_background
        when (activity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                color = R.color.md_theme_dark_background
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                color = R.color.md_theme_light_background
            }
        }
        val window = activity.window
        if (window != null) {
            context.let {
                window.statusBarColor = ContextCompat.getColor(it, color)
            }
        }
    }

    fun setSecondaryColorToStatusBar(activity: Activity, context: Context) {
        var color = R.color.md_theme_light_secondaryContainer
        when (activity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                color = R.color.md_theme_dark_secondaryContainer
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                color = R.color.md_theme_light_secondaryContainer
            }
        }
        val window = activity.window
        if (window != null) {
            context.let {
                window.statusBarColor = ContextCompat.getColor(it, color)
            }
        }
    }
}
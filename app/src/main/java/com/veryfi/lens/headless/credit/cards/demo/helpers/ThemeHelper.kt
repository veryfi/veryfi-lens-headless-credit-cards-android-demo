package com.veryfi.lens.headless.credit.cards.demo.helpers

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    fun applyEdgeToEdgeInsets(root: View, topTarget: View = root, bottomTarget: View = root) {
        val rootLeft = root.paddingLeft
        val rootTop = root.paddingTop
        val rootRight = root.paddingRight
        val rootBottom = root.paddingBottom
        val topInitial = topTarget.paddingTop
        val bottomInitial = bottomTarget.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowInsets ->
            val bars = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            if (topTarget === root && bottomTarget === root) {
                root.setPadding(
                    rootLeft + bars.left,
                    rootTop + bars.top,
                    rootRight + bars.right,
                    rootBottom + bars.bottom
                )
            } else {
                root.setPadding(rootLeft + bars.left, rootTop, rootRight + bars.right, rootBottom)
                topTarget.setPadding(
                    topTarget.paddingLeft,
                    topInitial + bars.top,
                    topTarget.paddingRight,
                    topTarget.paddingBottom
                )
                bottomTarget.setPadding(
                    bottomTarget.paddingLeft,
                    bottomTarget.paddingTop,
                    bottomTarget.paddingRight,
                    bottomInitial + bars.bottom
                )
            }
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.requestApplyInsets(root)
    }
}

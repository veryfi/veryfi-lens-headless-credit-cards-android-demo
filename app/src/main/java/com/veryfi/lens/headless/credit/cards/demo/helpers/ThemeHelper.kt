package com.veryfi.lens.headless.credit.cards.demo.helpers

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.veryfi.lens.headless.credit.cards.demo.R


object ThemeHelper {

//    fun getPrimaryColor(fragment: ContentFragment): Int {
//        var color = R.color.white
//        when (fragment.activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                color = R.color.md_theme_dark_primary
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                color = R.color.md_theme_light_primary
//            }
//        }
//        return color
//    }
//
//    fun getSecondaryColor(fragment: ContentFragment): Int {
//        var color = R.color.md_theme_light_secondaryContainer
//        when (fragment.activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                color = R.color.md_theme_dark_secondaryContainer
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                color = R.color.md_theme_light_secondaryContainer
//            }
//        }
//        return color
//    }

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
//
//    fun setPrimaryColorToStatusBar(fragment: ContentFragment, context: Context) {
//        var color = R.color.md_theme_light_primary
//        when (fragment.activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                color = R.color.md_theme_dark_primary
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                color = R.color.md_theme_light_primary
//            }
//        }
//        val window = fragment.activity?.window
//        if (window != null) {
//            context.let {
//                window.statusBarColor = ContextCompat.getColor(it, color)
//            }
//        }
//    }
}
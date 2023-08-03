package com.hyouteki.projects.orbiter.utils

import android.content.Context
import android.content.SharedPreferences

interface Saver {
    companion object {
        // Element tags
        const val LAUNCH_NEW_TAB = "LAUNCH_NEW_TAB"
        const val CURRENT_TAB = "CURRENT_TAB"
        const val TAB_LAYOUT = "TAB_LAYOUT"
        const val TAB_LIST_LAYOUT = "TAB_LIST_LAYOUT"
        const val TAB_CARD_LAYOUT = "TAB_CARD_LAYOUT"
        const val JAVA_SCRIPT_ENABLED = "JAVA_SCRIPT_ENABLED"
        const val SUPPORT_ZOOM = "SUPPORT_ZOOM"
        const val DISPLAY_ZOOM_CONTROLS = "DISPLAY_ZOOM_CONTROLS"
        const val ALLOW_CONTENT_ACCESS = "ALLOW_CONTENT_ACCESS"
        const val DOM_STORAGE_ENABLED = "DOM_STORAGE_ENABLED"
        const val PAUSE_HISTORY = "PAUSE_HISTORY"
        const val SEARCH_ENGINE = "SEARCH_ENGINE"
        const val BRAVE = "Brave"
        const val BING = "Bing"
        const val DUCK_DUCK_GO = "DuckDuckGo"
        const val ECOSIA = "Ecosia"
        const val GOOGLE = "Google"
        const val START_PAGE = "Startpage"
        const val TAB_MENU = "TAB_MENU"

        private const val TAG = "ORBITER"
        private const val MODE: Int = Context.MODE_PRIVATE
        fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(TAG, MODE)
        }

        fun getEditor(context: Context): SharedPreferences.Editor {
            return getPreferences(context).edit()
        }
    }
}
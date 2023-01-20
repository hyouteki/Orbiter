package com.hyouteki.orbiter.interfaces

import android.content.Context
import android.content.SharedPreferences

interface Preferences {
    companion object {
        private const val TAG = "SETTINGS"
        private const val MODE: Int = Context.MODE_PRIVATE
        fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(TAG, MODE)
        }

        fun getEditor(context: Context): SharedPreferences.Editor {
            return getPreferences(context).edit()
        }
    }
}
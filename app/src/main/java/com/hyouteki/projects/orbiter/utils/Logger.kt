package com.hyouteki.projects.orbiter.utils

import android.util.Log

interface Logger {
    companion object {
        private const val DEBUG_TAG = "DEBUG"
        fun debugger(message: String) {
            Log.d(DEBUG_TAG, message)
        }
    }
}
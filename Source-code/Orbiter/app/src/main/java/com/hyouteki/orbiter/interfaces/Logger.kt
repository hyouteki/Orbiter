package com.hyouteki.orbiter.interfaces

import android.util.Log

interface Logger {
    companion object {
        private const val TAG = "custom"

        fun exception(message: String) {
            Log.e(TAG, message)
        }

        fun debugger(message: String) {
            Log.d(TAG, message)
        }

        fun information(message: String) {
            Log.i(TAG, message)
        }

        fun raiseException(message: String): Throwable {
            return Exception(message)
        }
    }
}